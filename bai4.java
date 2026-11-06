package Code.UDP;

import java.net.*;
import java.nio.charset.StandardCharsets;

public class bai4 {
    public static void main(String[] args) throws Exception {
        InetAddress host = InetAddress.getByName("36.50.135.242");
        int port = 2208;
        String studentCode = "B23DCCN472"; // mã của tài khoản đang đăng nhập
        String qCode = "SikjowsR";         // copy bằng nút copy

        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(5000);

            // a. Gửi ";studentCode;qCode"
            byte[] s = (";" + studentCode + ";" + qCode).getBytes(StandardCharsets.UTF_8);
            socket.send(new DatagramPacket(s, s.length, host, port));

            // b. Nhận "requestId;data"
            byte[] buf = new byte[65535];
            DatagramPacket recv = new DatagramPacket(buf, buf.length);
            socket.receive(recv);
            String msg = new String(recv.getData(), 0, recv.getLength(), StandardCharsets.UTF_8).trim();
            System.out.println("Received: " + msg);

            int idx = msg.indexOf(';');
            String requestId = msg.substring(0, idx).trim();
            String data = msg.substring(idx + 1).trim();

            // c. Chuẩn hóa: hoa chữ đầu mỗi từ, còn lại thường
            StringBuilder sb = new StringBuilder();
            for (String w : data.split("\\s+")) {
                if (w.isEmpty()) continue;
                sb.append(Character.toUpperCase(w.charAt(0)))
                  .append(w.substring(1).toLowerCase())
                  .append(" ");
            }
            String result = requestId + ";" + sb.toString().trim();

            byte[] out = result.getBytes(StandardCharsets.UTF_8);
            socket.send(new DatagramPacket(out, out.length, recv.getAddress(), recv.getPort()));
            System.out.println("Sent: " + result);
        } // d. tự đóng socket
    }
}