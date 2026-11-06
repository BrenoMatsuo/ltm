
package Code.UDP;

import java.net.*;
import java.nio.charset.StandardCharsets;

public class bai3 {
    public static void main(String[] args) {

        try {
            // ==========================================
            // PHAN 1: KHOI TAO UDP CLIENT
            // ==========================================

            // Dia chi IP va cong Server
            String serverIP = "36.50.135.242";
            int serverPort = 2207;

            // Chuyen IP thanh InetAddress
            InetAddress serverAddress =
                InetAddress.getByName(serverIP);

            // Tao UDP Socket
            DatagramSocket socket = new DatagramSocket();


            // ==========================================
            // PHAN 2: GUI THONG TIN SINH VIEN
            // ==========================================

            String studentCode = "B23DCCN472";
            String qCode = "GpfBIvR4";

            // Tao chuoi theo dinh dang ;studentCode;qCode
            String message = ";" + studentCode + ";" + qCode;

            // Chuyen String thanh byte[]
            byte[] sendData =
                message.getBytes(StandardCharsets.UTF_8);

            // Tao goi tin UDP
            DatagramPacket sendPacket = new DatagramPacket(
                sendData,
                sendData.length,
                serverAddress,
                serverPort
            );

            // Gui thong tin len Server
            socket.send(sendPacket);


            // ==========================================
            // PHAN 3: NHAN DU LIEU TU SERVER
            // ==========================================

            // Tao bo dem nhan du lieu
            byte[] receiveData = new byte[1024];

            // Tao goi tin nhan
            DatagramPacket receivePacket = new DatagramPacket(
                receiveData,
                receiveData.length
            );

            // Nhan du lieu tu Server
            socket.receive(receivePacket);

            // Chuyen byte[] thanh String
            String receivedMessage = new String(
                receivePacket.getData(),
                0,
                receivePacket.getLength(),
                StandardCharsets.UTF_8
            );

            System.out.println("Server response: " + receivedMessage);


            // ==========================================
            
// PHAN 4: TACH DU LIEU

String[] parts = receivedMessage.split(";");

String requestId = parts[0];

String[] numbers = parts[1].split(",");


// PHAN 5: TIM MAX VA MIN

int max = Integer.parseInt(numbers[0]);
int min = Integer.parseInt(numbers[0]);

for (int i = 0; i < numbers.length; i++) {

    int number = Integer.parseInt(numbers[i]);

    if (number > max) {
        max = number;
    }

    if (number < min) {
        min = number;
    }
}

System.out.println("Max: " + max);
System.out.println("Min: " + min);


// PHAN 6: TAO KET QUA

String result = requestId + ";" + max + "," + min;

System.out.println("Ket qua gui Server: " + result);



            // ==========================================
            // PHAN 7: GUI KET QUA VE SERVER
            // ==========================================

            // Chuyen ket qua thanh byte[]
            byte[] resultData =
                result.getBytes(StandardCharsets.UTF_8);

            // Tao goi tin UDP chua ket qua
            DatagramPacket resultPacket = new DatagramPacket(
                resultData,
                resultData.length,
                serverAddress,
                serverPort
            );

            // Gui ket qua ve Server
            socket.send(resultPacket);


            // ==========================================
            // PHAN 8: DONG SOCKET
            // ==========================================

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
