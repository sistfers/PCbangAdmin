package Admin_starts;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


import Admin_views.Admin_OrderFrame;
import all_DTO.ProductDTO;

public class HostPcServer extends Thread {
	Vcontrol vc = Vcontrol.getInstance("호스트서버"); // 싱글톤불러오기;
	ServerReceiver receiver = null;
	
	 //서버스타트
	public void startFromFrame(int i) {
		if (i == 1)
			vc.selectFrame(1);
		else
			vc.selectFrame(2);
	}
	
	public void run() {
		ServerSocket serverSocket = null;
		Socket socket = null;

		try {
			serverSocket = new ServerSocket(7777);

			// 접속을 계속 받아내는 쓰레드~
			while (true) {
				socket = serverSocket.accept();

				receiver = new ServerReceiver(socket);
				receiver.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 내부 리시버 클래스 - 이 클래스는 연결이 생길때마다 계속 생긴다.
	class ServerReceiver extends Thread {
		Socket socket;
		DataInputStream in;
		DataOutputStream out;

		// 생성자에서는 서버소켓을 받아서 인풋아웃풋 스트림을 하나 만들고 연결한다~
		ServerReceiver(Socket socket) {
			this.socket = socket;

			try {
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {
			}
		}// 생성자 끝

		// 리시버 스타트~
		public void run() {
			int num = 0;
			String name = "";
			int age = 0;
			String realname = "";

			try {
				num = in.readInt();
				name = in.readUTF();
				age = in.readInt();
				realname = in.readUTF();
				
				// 컴퓨터 켜진 상태로 만든다.
				vc.turnOn(num);
				// 네트워크 연결중 요청 메시지 계속 받기 처리
				while (in != null) {
					String s = in.readUTF();
					switch (s) {
					case "로그인":
						vc.newSeat(num, name, socket);
						vc.login(num, name,realname,age);
						break;
					case "로그아웃":
						vc.logout(num);
						break; // 화면변환 메소드
					case "컴퓨터끔":
						break;
					case "메시지":
						String message = in.readUTF();
						vc.messageFromPC(num, message);
						break; // 메시지처리 메소드
					case "주문":
						int seatNum = in.readInt();   //자리
		                  String itemType = in.readUTF();   //상품분류
		                  String getItem = in.readUTF();   //상품명
		                  int getNum = in.readInt();   //상품 주문 갯수
		                  int getPrice = in.readInt();   //가격
		                  String userId = in.readUTF();
		                  
		                  ProductDTO orderProduct = new ProductDTO();
		                  orderProduct.setProductName(getItem);
		                  orderProduct.setQuantity(getNum);
		                  orderProduct.setPrice(getPrice);
		                  orderProduct.setSeat(seatNum);
		                  orderProduct.setId(userId);
		                  orderProduct.setProductType(itemType);
		                  
		                  vc.orderDao.addList(orderProduct);
		                  
		                  if(vc.takeFrame == null){
		                     vc.takeFrame = new Admin_OrderFrame(vc.orderDao);
		                  }else{
		                     vc.takeFrame.tablePrint(vc.orderDao);
		                     vc.takeFrame.setVisible(true);
		                  }
						break;
					}
				}
			} catch (IOException e) {
			} finally {
				// 컴퓨터 꺼지다
				vc.turnOff(num);
			}
		}// 런끝
	}
}
