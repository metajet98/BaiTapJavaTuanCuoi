package application;

import java.sql.Date;

public class GiangDay {
	
	String TenGV;
	String MaLop;
	String TenMon;
	int STT;
	int SiSo;
	Date NgayStart;
	Date NgayEnd;
	int SoTiet;
	
	public GiangDay(String tenGV, String maLop, String tenMon, int sTT, int siSo, Date ngayStart, Date ngayEnd,
			int soTiet) {
		super();
		TenGV = tenGV;
		MaLop = maLop;
		TenMon = tenMon;
		STT = sTT;
		SiSo = siSo;
		NgayStart = ngayStart;
		NgayEnd = ngayEnd;
		SoTiet = soTiet;
	}

	public String getTenGV() {
		return TenGV;
	}

	public void setTenGV(String tenGV) {
		TenGV = tenGV;
	}

	public String getMaLop() {
		return MaLop;
	}

	public void setMaLop(String maLop) {
		MaLop = maLop;
	}

	public String getTenMon() {
		return TenMon;
	}

	public void setTenMon(String tenMon) {
		TenMon = tenMon;
	}

	public int getSTT() {
		return STT;
	}

	public void setSTT(int sTT) {
		STT = sTT;
	}

	public int getSiSo() {
		return SiSo;
	}

	public void setSiSo(int siSo) {
		SiSo = siSo;
	}

	public Date getNgayStart() {
		return NgayStart;
	}

	public void setNgayStart(Date ngayStart) {
		NgayStart = ngayStart;
	}

	public Date getNgayEnd() {
		return NgayEnd;
	}

	public void setNgayEnd(Date ngayEnd) {
		NgayEnd = ngayEnd;
	}

	public int getSoTiet() {
		return SoTiet;
	}

	public void setSoTiet(int soTiet) {
		SoTiet = soTiet;
	}
	
	

}
