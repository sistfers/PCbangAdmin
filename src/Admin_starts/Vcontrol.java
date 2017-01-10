package Admin_starts;

/**
 * GUI에서 하는 대부분의 일들을 처리해주는 중앙집권클래스이다.
 * 하는 일 순서도 
 * 
 //00. 바깥의 프레임에서 메인프레임을 보이게 한다.
 //01. 컨트롤타워 싱글톤만들기
 //02. 새로운 자리받는 메소드
 //03. 컴퓨터 켜짐 from HostPcServer
 //04. 컴퓨터 꺼짐 from HostPcServer
 //05. 로그인 처리 from HostPcServer
 //06. 로그아웃처리 from HostPcServer
 //07. 나머지 따옴표 처리 from HostPcServer
 //08. 계속 좌석 계산해주고 바로 밑에 센드 from Seat
 //09. 계산메소드 from HostPcServer
 //10. 클라이언트로 부터 받은 메시지
 //11. 단체계산
 * 유지보수 일지
 * 여기서 모든 Login HashMap을 가지고 있기로 하였다.
 * 번호 파라미터 하나만 받아서 모두 처리하도록.. 
 */
import java.awt.Color;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import Admin_DAO.ADProductDAO;
import Admin_DAO.OrderDAO;
import Admin_DAO.SalesHisDAO;
import Admin_dbfiles.DBConnectionMgr;
import Admin_viewHUD.Login_Fr_Hud;
import Admin_viewHUD.Manage_Fr_Hud;
import Admin_views.ChatFrame;
import Admin_views.Admin_OrderFrame;
import Admin_views.adb_Manage;
import _client_Boards.AccountDAO;
import _client_Boards.BoardDAO;
import all_DTO.Seat;

public class Vcontrol {

	Socket socket;
	DataOutputStream out;
	private boolean stopped = false;
	
	// 핵심 필드 : 좌석 객체모델을 저장함과 동시에 소켓을 저장한다.
	public HashMap<Seat, Socket> clients = new HashMap<Seat, Socket>();
	public Seat[] pcseat = new Seat[50];
	public adb_Manage mf;

	public static ChatFrame[] chatClient = new ChatFrame[50];
	static Login_Fr_Hud login_Fr_Hud;

	Date today = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
	String time = sdf.format(today);

	public OrderDAO orderDao = new OrderDAO();
	public Admin_OrderFrame takeFrame;
	public BoardDAO boardDao = new BoardDAO();
	public AccountDAO accountDao = new AccountDAO();
	public ADProductDAO productDao = new ADProductDAO();
	public SalesHisDAO salesHisDao = new SalesHisDAO();
	
	
	public static void main(String[] args) {

		login_Fr_Hud = new Login_Fr_Hud();

	}

	// 00.바깥의 프레임에서 메인프레임을 보이게 한다.

	public void mainFrameHUD() {
		mf = new Manage_Fr_Hud();

		Thread host = new HostPcServer();
		host.start();

	}

	// 메인 관리 프레임선택 (삽질한 것)
	public void selectFrame(int i) {
		if (i == 1)
			mf = new Manage_Fr_Hud();

	}

	// 01.컨트롤타워 싱글톤만들기
	private static Vcontrol instance = new Vcontrol();

	public static Vcontrol getInstance() {
		return instance;
	}

	public static Vcontrol getInstance(String s) {
		return instance;
	}

	private Vcontrol() {
		// 동기화시켜서 쓰레드간 비동기화발생하지않도록
		Collections.synchronizedMap(clients);
	}

	// 02.새로운 자리받는 메소드 소켓할당
	public void newSeat(int num, String name, Socket socket) {
		pcseat[num] = new Seat(num, name);
		clients.put(pcseat[num], socket);
	}

	// 03.컴퓨터켜짐 from HostPcServer
	public void turnOn(int num) {
		mf.pan[num].setBackground(Color.white);
		mf.pan[num].label[1].setText("자리 켜짐");
		mf.pan[num].turnOn();
		ddaom(num);

	}

	// 04.컴퓨터 꺼짐 from HostPcServer
	public void turnOff(int num) {
		mf.pan[num].setBackground(Color.gray);
		mf.pan[num].label[0].setText((num + 1) + ". 빈자리");
		ddaom(num);
		mf.pan[num].turnOff();
	}

	// 05.로그인 처리 from HostPcServer
	public void login(int num, String name,String realname,int age) {
		int money = pcseat[num].getMoney();
		mf.pan[num].setBackground(Color.blue);
		mf.pan[num].label[0].setForeground(Color.red);
		mf.pan[num].label[0].setText((num + 1) + ". 로그인");
		mf.pan[num].label[1].setText(name + "님");
		mf.pan[num].label[2].setText("");
		mf.pan[num].label[3].setText(money + "원");
		mf.pan[num].isLogined = true;
		mf.pan[num].nickname = name;
		mf.pan[num].starttimes = time;
		mf.pan[num].realname = realname;
		mf.pan[num].age = age;
		pcseat[num].setUsername(name);
		pcseat[num].setLogin(true);
		
		accountDao.setSeat((num+1), name); // 로그인시 좌석 값 설정로..
		
		if (!pcseat[num].isFirst()) {
			pcseat[num].setFirst(true);
			pcseat[num].start();
		}
		
		mf.progress.setValue(mf.progress.getValue() + 2);
	}

	// 06.로그아웃처리 from HostPcServer
	public void logout(int num) {
		pcseat[num].interrupt();
		mf.pan[num].setBackground(Color.white);
		mf.pan[num].label[0].setForeground(Color.black);
		mf.pan[num].label[0].setText((num + 1) + ". 빈자리");
		mf.pan[num].age = 0;
		mf.pan[num].realname = null;
		mf.pan[num].starttimes = null;
		mf.pan[num].gametimes = null;
		mf.pan[num].totalmoney = 0;
		ddaom(num);
		mf.pan[num].isLogined = false;
		// 인터럽트시켜서 요금 올리기 중지
		accountDao.setSeat(0, mf.pan[num].nickname); // 로그아웃시 좌석 null로.. 0을 null로 간주
		pcseat[num].interrupt();
		pcseat[num].setLogin(false);

		try {
			socket = clients.get(pcseat[num]);
			out = new DataOutputStream(socket.getOutputStream());
			out.writeUTF("로그아웃");
		
			// 클라이언트에서 없애기
			clients.remove(pcseat[num]);
		} catch (IOException e) {
		}
		
		mf.progress.setValue(mf.progress.getValue() - 2);
	}

	// 07.나머지 따옴표 처리 from HostPcServer
	public void ddaom(int num) {
		mf.pan[num].label[1].setText("");
		mf.pan[num].label[2].setText("");
		mf.pan[num].label[3].setText("");

	}

	// 08. 계속 좌석 계산해주고 바로 밑에 센드 from Seat
	public void continueMoney(int num, Calendar date) {

		int money = pcseat[num].getMoney();
		mf.pan[num].label[3].setText(money + "원");
		mf.pan[num].totalmoney = money;
		Calendar dateAfter = Calendar.getInstance();
		dateAfter.setTimeInMillis(System.currentTimeMillis());

		long differ = (dateAfter.getTimeInMillis() - date.getTimeInMillis()) / (1000);
		long differ_hour = differ / 60;

		String gametime = differ_hour + "분" + (differ % 60) + "초";
		mf.pan[num].label[2].setText(gametime);
		mf.pan[num].gametimes = gametime;
		// 바로 밑에 클라이언트로 사용시간 보내주는 메소드 실행
		sendTime(pcseat[num], money, gametime);
	}

	public void sendTime(Seat pcseat, int money, String gametime) {
		try {
			out = new DataOutputStream(clients.get(pcseat).getOutputStream());
			out.writeUTF("요금정보");
			out.writeInt(money);
			out.writeUTF(gametime);
		} catch (IOException e) {
		}
	}



	// 10. 클라이언트로 부터 받은 메시지
	public void messageFromPC(int num, String message) {
		if (chatClient[num] == null)
			chatClient[num] = new ChatFrame(num);
		chatClient[num].setVisible(true);
		chatClient[num].ta.append(message);
	}

}
