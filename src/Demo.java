import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Set;

public class Demo {
    static ArrayList<Set<String>> listBagOfNonSpam = new ArrayList<>();
    static ArrayList<Set<String>> listBagOfSpam = new ArrayList<>();

    public static double pNonSpam(String x){
        double k = 0;
        for (int i =0; i< listBagOfNonSpam.size();i++){
            if (listBagOfNonSpam.get(i).contains(x)){
                k++;
            }
        }
        return (k+1)/(listBagOfNonSpam.size()+1);
    }
    public static double pSpam(String x){
        double k =0;
        for (int i=0;i<listBagOfSpam.size();i++){
            if (listBagOfSpam.get(i).contains(x)){
                k++;
            }
        }
        return (k+1)/(listBagOfSpam.size());
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("Start training");
        ObjectInputStream inp = new ObjectInputStream(new FileInputStream(
                new File("data/result/result_training.dat")
        ));
        listBagOfSpam = (ArrayList<Set<String>>) inp.readObject();
        listBagOfNonSpam = (ArrayList<Set<String>>) inp.readObject();
        inp.close();
        System.out.println("Finished training");
        System.out.println("Start test email");

        File mailTesting = new File("data/test/test (2).txt");
        String mailData = FileUtils.readFileToString(mailTesting, "UTF-16");
        Set<String> bagOfTest = RunTrainingData.toBagOfWord(mailData);

        System.out.println("Bắt đầu kiểm tra:");

        // xác xuất là thư thường. P(xi|non-spam)
        double C_NB1 = listBagOfNonSpam.size() / ((double) listBagOfNonSpam.size() + listBagOfSpam.size());
        // xác xuất là thư rác. P(xi|spam)
        double C_NB2 = listBagOfSpam.size() / ((double) listBagOfNonSpam.size() + listBagOfSpam.size());

        ArrayList<String> listStringTest = new ArrayList<>(bagOfTest);

        for (String strTest : listStringTest) {
            if (pNonSpam(strTest) != ((double) 1 / (listBagOfNonSpam.size() + 1))
                    || pSpam(strTest) != ((double) 1 / (listBagOfSpam.size() + 1))) {

                System.out.println("P(x_i=" + strTest + "|nonspam)=  " + pNonSpam(strTest) + "        " + "P(x_i="
                        + strTest + "|spam)=  " + pSpam(strTest));
                C_NB1 *= pNonSpam(strTest);
                C_NB2 *= pSpam(strTest);
            }
        }
        if (C_NB1 < C_NB2) {
            // Bổ sung thư vừa kiểm tra vào tập huấn luyện.
            listBagOfSpam.add(bagOfTest);
            System.out.println("Là thư rác");
        } else {
            listBagOfNonSpam.add(bagOfTest);
            System.out.println("Là thư thường");
        }

        // Lưu lại tập huấn luyện mới.
        ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(new File("data/result/result_training.dat")));
        out.writeObject(listBagOfSpam);
        out.writeObject(listBagOfNonSpam);
        out.close();
        System.out.println("Kết thúc");
    }
}
