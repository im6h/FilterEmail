import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class RunTrainingData {
    public static Set<String> toBagOfWord(String s) {
        HashSet<String> bag = new HashSet<>();
        // tách các từ phân cách bởi các dấu ,.!*"'()
        StringTokenizer s1 = new StringTokenizer(s, " ,.!*\"\'()");
        while (s1.hasMoreTokens()) {
            bag.add(s1.nextToken());
        }
        return bag;
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Bắt đầu huấn luyện...");

        // Đọc tất cả các file thư rác (spam), chuyển thành danh sách các túi từ
        File folderSpam = new File("data/spam");
        ArrayList<Set<String>> listBagOfSpam = new ArrayList<>();
        File[] spams = folderSpam.listFiles();
        for (File file : spams) {
            String fileData = FileUtils.readFileToString(file, "UTF-16");
            Set<String> bagOfWord = toBagOfWord(fileData);
            listBagOfSpam.add(bagOfWord);
        }

        // Đọc tất cả các file thư thường (non-spam), chuyển thành danh sách các
        // túi từ
        File folderNonSpam = new File("data/non-spam");
        ArrayList<Set<String>> listBagOfNonSpam = new ArrayList<>();
        File[] nonSpams = folderNonSpam.listFiles();
        for (File file : nonSpams) {
            String fileData = FileUtils.readFileToString(file, "UTF-16");
            Set<String> bagOfWord = toBagOfWord(fileData);
            listBagOfNonSpam.add(bagOfWord);
        }

        ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(new File("data/result/result_training.dat")));
        out.writeObject(listBagOfSpam);
        out.writeObject(listBagOfNonSpam);
        out.close();
        System.out.println("Hoàn thành huấn luyện");
    }
}
