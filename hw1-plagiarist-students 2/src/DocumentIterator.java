import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

public class DocumentIterator implements Iterator<String> {


    private Reader r;
    private int    c = -1;

    private int n;

    List<String> listWords = new ArrayList<>();
    

    public DocumentIterator(Reader r, int n) {
        this.r = r;
        this.n = n;
        skipNonLetters();
    }


    private void skipNonLetters() {
        try {
            this.c = this.r.read();
            while (!Character.isLetter(this.c) && this.c != -1) {
                this.c = this.r.read();
            }
        } catch (IOException e) {
            this.c = -1;
        }
    }


    @Override
    public boolean hasNext() {
        return (c != -1);
    }


    @Override
    public String next() {

        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        String finalans = "";
        String answer = "";
        try {
            if (listWords.isEmpty()){

                for(int i = 0 ; i < this.n; i++){
                    answer = "";
                    while (Character.isLetter(this.c)) {
                        answer = answer + (char)this.c;
                        this.c = this.r.read();
                    }
                    listWords.add(answer);
                    skipNonLetters();
                }
            } else {
                listWords.remove(0);
                while (Character.isLetter(this.c)) {
                    answer = answer + (char) this.c;
                    this.c = this.r.read();
                }
                listWords.add((answer));
                skipNonLetters();
            }
        } catch (IOException e) {
            throw new NoSuchElementException();
        }


        for (int i = 0; i < listWords.size(); i++) {
            for (int j = 0; j <listWords.get(i).length(); j++) {
                finalans += listWords.get(i).charAt(j);
            }
        }
        return finalans.toLowerCase();
    }

}
