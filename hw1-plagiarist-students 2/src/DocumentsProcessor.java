import java.io.*;
import java.util.*;

public class DocumentsProcessor implements IDocumentsProcessor {


    @Override
    public Map<String, List<String>> processDocuments(String directoryPath, int n) {
        //Creating a list of directory names
        File dir = new File(directoryPath);
        File[] dirList = dir.listFiles();
        //Creating a Map that maps strings to lists
        Map<String, List<String>> docs = new HashMap<>();

        //For loop to loop through the file list
        int listLength = dirList.length;
        for (int i = 0 ; i < listLength; i++){
            if(!dirList[i].getName().equals(".DS_Store")){
                try {
                    //Set the variables of file reader and buffer reader and document iterator
                    List listOfStrings = new ArrayList<>();
                    FileReader fr = new FileReader(dirList[i]);
                    BufferedReader br = new BufferedReader(fr);
                    DocumentIterator di = new DocumentIterator(br, n);

                    while(di.hasNext()){
                        listOfStrings.add(di.next());
                    }
                    docs.put(dirList[i].getName(), listOfStrings);


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return docs;
    }

    @Override
    public List<Tuple<String, Integer>> storeNGrams(Map<String, List<String>> docs, String nGramFilePath) {
        List<Tuple<String, Integer>> nGrams = new ArrayList<>();
        try {
            FileWriter fw = new FileWriter(nGramFilePath);
            for (Map.Entry<String, List<String>> entry : docs.entrySet()){
                int numBytes = 0;
                String fileName = entry.getKey();
                List<String> nGramsList = entry.getValue();
                for (int i = 0; i < nGramsList.size(); i++){
                    try {
                        fw.write(nGramsList.get(i) + " ");
                        numBytes += nGramsList.get(i).length() + 1;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Tuple<String, Integer> result = new Tuple<>(fileName, numBytes);
                nGrams.add(result);
            }
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nGrams;
    }

    @Override
    public TreeSet<Similarities> computeSimilarities(String nGramFilePath, List<Tuple<String, Integer>> fileIndex) {
        TreeSet<Similarities> stm = new TreeSet<>();
        try {
            FileInputStream fis = new FileInputStream(nGramFilePath);
            HashMap<String, Set<String>> hm = new HashMap<>();
            for (int i = 0; i < fileIndex.size(); i++){
                String nString = "";
                int totalBytes = fileIndex.get(i).getRight();
                String fileName = fileIndex.get(i).getLeft();
                int bytesRead = 0;
                while(bytesRead < totalBytes){
                    char c = (char) fis.read();
                    bytesRead += 1;
                    if (c != ' '){
                        nString += c;
                    } else {
                        if (!hm.containsKey(nString)){
                            Set<String> fl = new HashSet<>();
                            fl.add(fileName);
                            hm.put(nString, fl);
                        } else {
                            Set<String> fl = hm.get(nString);
                            if (!fl.contains(fileName)){
                                for (String nGrams : fl){
                                    if(!fileName.equals(nGrams)){
                                        Similarities sim = new Similarities(fileName, nGrams);
                                        boolean found = false;
                                        for (Similarities s : stm){
                                            if (sim.compareTo(s) == 0){
                                                s.setCount(s.getCount()+1);
                                                found = true;
                                                break;
                                            }
                                        }
                                        if (!found){
                                            stm.add(sim);
                                            sim.setCount(1);
                                        }
                                    }
                                }
                                hm.get(nString).add(fileName);
                            }

                        }
                        nString = "";
                    }
                }
            }
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return stm;
    }

    @Override
    public void printSimilarities(TreeSet<Similarities> sims, int threshold) {
        List<Similarities> descendedList = new ArrayList<>();
        for (Similarities list : sims) {
            if(list.getCount() > threshold){
                descendedList.add(list);
            }
        }
        descendedList.sort((a,b) -> b.getCount() - a.getCount());
        for (Similarities sim : descendedList) {
            System.out.println(sim.getFile1() + " is similar to " + sim.getFile2() + " with " + sim.getCount() + " nGrams Similarity");
        }
    }

    public List<Tuple<String, Integer>> processAndStore(String directoryPath,
                                                 String sequenceFile,
                                                 int n) {
        List<Tuple<String, Integer>> nList = new ArrayList<>();

        File dir = new File(directoryPath);
        File[] dirList = dir.listFiles();


        for (int i =0; i < dirList.length; i++){
            int numBytes = 0;
            if (!dirList[i].equals(".DS_Store")) {
                try {
                    FileWriter fw = new FileWriter(sequenceFile);
                    BufferedReader br = new BufferedReader(new FileReader(dirList[i]));
                    DocumentIterator iter = new DocumentIterator(br, n);
                    while (iter.hasNext()) {
                        String nStr = iter.next();
                        fw.write(nStr + " ");
                        numBytes +=nStr.length() + 1;
                    }
                    fw.flush();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();

                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                Tuple<String, Integer> nGrams = new Tuple<>(dirList[i].getName(), numBytes-1);
                nList.add(nGrams);
            }
        }
        return nList;
    }
}




