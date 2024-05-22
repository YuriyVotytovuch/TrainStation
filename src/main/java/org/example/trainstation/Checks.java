package org.example.trainstation;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.example.trainstation.database.Database;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Checks extends Database{

    public static void generateCheck(String trainnumber, String from, String to, String date, String time, String price) throws IOException, WriterException {
        // Створення каталогу "checks", якщо він не існує
        Path checksPath = Paths.get("checks");
        if (!Files.exists(checksPath)) {
            Files.createDirectory(checksPath);
        }

        // Створення даних користувача
        String name = isloginuse();

        // Створення дати та часу
        String dateTime = date;

        // Створення інформації про подорож
        String travelDirection = (from+" - "+to);
        String trainNumber = trainnumber;
        String priceTicket = price;

        // Генерування QR-коду
        String qrText = (name + "," + dateTime + "," + travelDirection + "," + trainNumber + "," + priceTicket);
        BufferedImage qrImage = generateQRCode(qrText, 200, 200);

        // Збереження QR-коду у файл
        Path qrFilePath = Paths.get("checks", "qr.png");
        ImageIO.write(qrImage, "png", qrFilePath.toFile());

        // Створення тексту квитанції
        String checkname = "**Чек**\n\n";
        String username = "Ім'я: " + name;
        String datebuy = "Дата поїздки: " + dateTime;
        String direction = "Напрямок руху: " + travelDirection;
        String trainn = trainNumber;
        // Створення зображення квитанції
        BufferedImage receiptImage = new BufferedImage(400, 800, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = receiptImage.createGraphics();
        // Встановлюємо колір фону (білий)
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, receiptImage.getWidth(), receiptImage.getHeight());
        // Встановлюємо колір тексту (чорний)
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.PLAIN, 24));
        g2d.drawString(checkname, 150, 20);
        g2d.setFont(new Font("Arial", Font.PLAIN, 20));
        g2d.drawString(username, 10, 100);
        g2d.setFont(new Font("Arial", Font.PLAIN, 20));
        g2d.drawString(datebuy, 10, 140);
        g2d.setFont(new Font("Arial", Font.PLAIN, 20));
        g2d.drawString(direction, 10, 180);
        g2d.setFont(new Font("Arial", Font.PLAIN, 20));
        g2d.drawString("Номер потягу: " + trainn, 10, 220);
        g2d.setFont(new Font("Arial", Font.PLAIN, 20));
        g2d.drawString("Ціна квитка: " + price + "₴", 10, 260);
        g2d.setFont(new Font("Arial", Font.PLAIN, 20));
        g2d.drawString("Сьогодні ваш день буде прекрасним.", 10, 770);
        // Завершуємо роботу з графічним контекстом
        g2d.dispose();

        // Збереження квитанції у файл
        Path receiptFilePath = Paths.get("checks", "receipt.png");
        ImageIO.write(receiptImage, "png", receiptFilePath.toFile());

        // Об'єднання QR-коду та квитанції в одне зображення
        BufferedImage combinedImage = new BufferedImage(400, 800, BufferedImage.TYPE_INT_RGB);
        g2d = combinedImage.createGraphics();
        g2d.drawImage(receiptImage, 0, 0, null);
        g2d.drawImage(qrImage, 100, 450, null);
        // Збереження об'єднаного зображення у файл
        Path combinedFilePath = Paths.get("checks", "combined.png");
        ImageIO.write(combinedImage, "png", combinedFilePath.toFile());

        // Вивід повідомлення про успішне створення файлу
        System.out.println("File generated: " + combinedFilePath.toString());
        Runtime.getRuntime().exec("cmd /c start " + combinedFilePath);
    }

    // Метод для генерації QR-коду
    private static BufferedImage generateQRCode(String qrText, int width, int height) throws WriterException {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = new QRCodeWriter().encode(qrText, BarcodeFormat.QR_CODE, width, height, hints);

        // Перетворення BitMatrix у BufferedImage
        BufferedImage qrImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                qrImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return qrImage;
    }
    public static void generateQRCode(int width, int height) throws WriterException, IOException {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        String qrText = "https://t.me/TrainStationHelp_bot";
        BitMatrix bitMatrix = new QRCodeWriter().encode(qrText, BarcodeFormat.QR_CODE, width, height, hints);

        // Перетворення BitMatrix у BufferedImage
        BufferedImage qrImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                qrImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        Path qrFilePath = Paths.get("checks", "helpbotqr.png");
        ImageIO.write(qrImage, "png", qrFilePath.toFile());
    }
}