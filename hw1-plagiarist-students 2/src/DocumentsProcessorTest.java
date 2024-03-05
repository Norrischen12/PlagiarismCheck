import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DocumentsProcessorTest {
    DocumentsProcessor doc = new DocumentsProcessor();

    @Test
    public void testProcessedDocuments(){
        Map<String, List<String>> testMap = doc.processDocuments("src/TestCases", 4);
        List<String> testNGrams = testMap.get("file1.txt");
        String testNGram0 = testNGrams.get(0);
        String testNGram2 = testNGrams.get(2);
        String testNGram4 = testNGrams.get(4);
        String testNGram5 = testNGrams.get(5);
        String expected0 = "noonecancopy";
        String expected2 = "cancopyfromme";
        String expected4 = "frommeyoucan";
        String expected5 = "meyoucantry";

        assertEquals(testNGram0, expected0);
        assertEquals(testNGram2, expected2);
        assertEquals(testNGram4, expected4);
        assertEquals(testNGram5, expected5);
    }

    @Test
    public void testStoreNGrams(){
        Map<String, List<String>> testMap = doc.processDocuments("src/StoreNGrams", 4);
        List<Tuple<String, Integer>> testNGrams = doc.storeNGrams(testMap, "src/ProcessorTest.txt");

        Tuple<String, Integer> expected1 = new Tuple<>("file1.txt", 28);
        String expected11 = expected1.getLeft();
        Integer expected12 = expected1.getRight();
        Tuple<String, Integer> expected2 = new Tuple<>("file2.txt", 42);
        String expected21 = expected2.getLeft();
        Integer expected22 = expected2.getRight();

        assertEquals(testNGrams.get(0).getLeft(), expected11);
        assertEquals(testNGrams.get(1).getLeft(), expected21);

        assertEquals(testNGrams.get(0).getRight(), expected12);
        assertEquals(testNGrams.get(1).getRight(), expected22);

    }
    @Test
    public void testComputeSimilarities(){
        Map<String, List<String>> testMap = doc.processDocuments("src/TestSet", 3);
        List<Tuple<String, Integer>> testNGrams = doc.storeNGrams(testMap, "src/ProcessorTest.txt");
        TreeSet <Similarities> ts = doc.computeSimilarities("src/ProcessorTest.txt",testNGrams);

        Similarities[] arr = new Similarities[3];
        int i = 0;
        for (Similarities value : ts) {
            arr[i] = value;
            i++;
        }

        String expected1 = arr[0].getFile1();
        String expected2 = arr[0].getFile2();
        Integer expected3 = arr[0].getCount();

        assertEquals(expected1, "file2.txt");
        assertEquals(expected2, "file3.txt");
        assertEquals(expected3, 3);

        doc.printSimilarities(ts,2);
    }

    @Test
    public void testProcessAndStore(){
        List<Tuple<String, Integer>> arr = doc.processAndStore("src/StoreNGrams", "ProcessorTest.txt", 3);
        Tuple<String, Integer> expected1 = new Tuple<>("file2.txt", 42);
        String expected11 = expected1.getLeft();
        Integer expected12 = expected1.getRight();
        Tuple<String, Integer> expected2 = new Tuple<>("file1.txt", 29);
        String expected21 = expected2.getLeft();
        Integer expected22 = expected2.getRight();

        assertEquals(arr.get(0).getLeft(), expected11);
        assertEquals(arr.get(1).getLeft(), expected21);

        assertEquals(arr.get(0).getRight(), expected12);
        assertEquals(arr.get(1).getRight(), expected22);
    }




}
