package Code.UDP;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class bai1 {
    public static void main(String[] args) {
        DatagramSocket socket = null;

        try {
            // Thông tin Server
            String serverHost = "36.50.135.242"; // Thay bằng IP Server đề bài cung cấp
            int serverPort = 2207;

            InetAddress serverAddress = InetAddress.getByName(serverHost);

            // Tạo UDP Socket
            socket = new DatagramSocket();

            // a. Gửi mã sinh viên và mã câu hỏi
            String studentCode = "B23DCCN472";
            String qCode = "BsN6ZTxx";

            String sendData = ";" + studentCode + ";" + qCode;

            byte[] sendBuffer = sendData.getBytes(StandardCharsets.UTF_8);

            DatagramPacket sendPacket = new DatagramPacket(
                sendBuffer,
                sendBuffer.length,
                serverAddress,
                serverPort
            );

            socket.send(sendPacket);

            // b. Nhận dữ liệu từ Server
            byte[] receiveBuffer = new byte[1024];

            DatagramPacket receivePacket = new DatagramPacket(
                receiveBuffer,
                receiveBuffer.length
            );

            socket.receive(receivePacket);

            String receivedData = new String(
                receivePacket.getData(),
                0,
                receivePacket.getLength(),
                StandardCharsets.UTF_8
            );

            System.out.println("Du lieu nhan: " + receivedData);

            // Phân tích dữ liệu: requestId;n;A1,A2,...,Am
            String[] parts = receivedData.split(";");

            String requestId = parts[0];
            int n = Integer.parseInt(parts[1]);

            String[] numbers = parts[2].split(",");

            // c. Tìm các số còn thiếu từ 1 đến n
            Set<Integer> existingNumbers = new HashSet<>();

            for (String number : numbers) {
                existingNumbers.add(Integer.parseInt(number.trim()));
            }

            List<Integer> missingNumbers = new ArrayList<>();

            for (int i = 1; i <= n; i++) {
                if (!existingNumbers.contains(i)) {
                    missingNumbers.add(i);
                }
            }

            // Tạo chuỗi kết quả
            StringBuilder result = new StringBuilder(requestId);
            result.append(";");

            for (int i = 0; i < missingNumbers.size(); i++) {
                result.append(missingNumbers.get(i));

                if (i < missingNumbers.size() - 1) {
                    result.append(",");
                }
            }

            System.out.println("Ket qua gui: " + result);

            // Gửi kết quả lên Server
            byte[] resultBuffer = result.toString()
                    .getBytes(StandardCharsets.UTF_8);

            DatagramPacket resultPacket = new DatagramPacket(
                resultBuffer,
                resultBuffer.length,
                serverAddress,
                serverPort
            );

            socket.send(resultPacket);

            // d. Đóng socket
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}
