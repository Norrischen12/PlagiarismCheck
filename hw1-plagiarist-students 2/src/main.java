
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class main {
    public static void main(String[] args){
        IDocumentsProcessor idp = new DocumentsProcessor();
        DocumentsProcessor dp = new DocumentsProcessor();
        Map<String, List<String>> res = idp.processDocuments("src/TestSet", 3);
        List <Tuple<String, Integer>> las = idp.storeNGrams(res, "/Users/norrischen12/Desktop/Grad School/Spring 2023/CIT 594/hw1-plagiarist-students/src/test1.txt");
        //List <Tuple<String, Integer>> las = dp.processAndStore("/Users/norrischen12/Desktop/Grad School/Spring 2023/CIT 594/hw1-plagiarist-students/src/TestSet",
                //"/Users/norrischen12/Desktop/Grad School/Spring 2023/CIT 594/hw1-plagiarist-students/src/test1.txt", 4);

        TreeSet <Similarities> ts = idp.computeSimilarities("/Users/norrischen12/Desktop/Grad School/Spring 2023/CIT 594/hw1-plagiarist-students/src/test1.txt", las);
        //for (Similarities value : ts) {
            //System.out.print("file1="+ valu=e.getFile1() + ", " +"file2="+ value.getFile2() + ", " + "count= "+ value.getCount());
        //}
        idp.printSimilarities(ts, 0);
        }
    }
