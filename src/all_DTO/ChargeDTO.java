package all_DTO;
public class ChargeDTO {

	private String today;
	private String minutes;
	private int payment;
	private String id;
	private int seat;
	
	public ChargeDTO() {
		
	}

	public String getToday() {
		return today;
	}

	public void setToday(String tempDate) {
		this.today = tempDate;
	}

	public String getMinutes() {
		return minutes;
	}

	public void setMinutes(String minutes) {
		this.minutes = minutes;
	}

	public int getPayment() {
		return payment;
	}

	public void setPayment(int payment) {
		this.payment = payment;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getSeat() {
		return seat;
	}

	public void setSeat(int seat) {
		this.seat = seat;
	}
	
}
