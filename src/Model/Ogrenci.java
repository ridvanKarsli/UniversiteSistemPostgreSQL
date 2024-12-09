package Model;

public class Ogrenci {
	
	private int id;
	private String ad;
	private String Soyad;
	private int okulNo;
	private Cinsiyet cinsiyet;
	
	public Ogrenci() {
		
	}
	
	public Ogrenci(String ad, String soyad, int okulNo, Cinsiyet cinsiyet) {
		this.ad = ad;
		Soyad = soyad;
		this.okulNo = okulNo;
		this.cinsiyet = cinsiyet;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAd() {
		return ad;
	}
	public void setAd(String ad) {
		this.ad = ad;
	}
	public String getSoyad() {
		return Soyad;
	}
	public void setSoyad(String soyad) {
		Soyad = soyad;
	}
	public int getOkulNo() {
		return okulNo;
	}
	public void setOkulNo(int okulNo) {
		this.okulNo = okulNo;
	}
	public Cinsiyet getCinsiyet() {
		return cinsiyet;
	}
	public void setCinsiyet(Cinsiyet cinsiyet) {
		this.cinsiyet = cinsiyet;
	}
	
	
}
