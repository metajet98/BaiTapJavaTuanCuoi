package application;

import java.awt.event.MouseAdapter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import com.mysql.cj.xdevapi.Table;

import java.util.Date;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import javafx.fxml.Initializable;

public class MainController implements Initializable{
	 	@FXML
	    private TextField txtSTT;
	 	@FXML
	    private TextField txtGV;

	    @FXML
	    private TextField txtSiSo;

	    @FXML
	    private Label txtSoLopKetThuc;

	    @FXML
	    private TextField txtTenMon;

	    @FXML
	    private DatePicker dtpNgayEnd;

	    @FXML
	    private DatePicker dtpNgayStart;

	    @FXML
	    private Label txtSoLop;

	    @FXML
	    private Label txtSoLopDienRa;

	    @FXML
	    private TableView tbvLichGiangDay;

	    @FXML
	    private Label txtLopNhieuNhat;

	    @FXML
	    private Label txtLopItNhat;

	    @FXML
	    private TextField txtSoTiet;

	    @FXML
	    private Label txtSoGV;

	    @FXML
	    private TextField txtMaLop;
	    @FXML
	    private Button btnThem;

	    @FXML
	    private Button btnSua;
	    
	    @FXML
	    private Button btnXoa;

	    @FXML
	    private Button btnMoCSDL;
	    @FXML
	    private TableView tbwThongKe;
	    
	    private ObservableList<ObservableList> data= FXCollections.observableArrayList();
	    private ObservableList<ObservableList> dataCheckGV= FXCollections.observableArrayList();
		
		Connection connection = null;
	    PreparedStatement preparedStatement = null;
	    Statement statement= null;
	    ResultSet resultSet = null;
	    

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		DisableButtonWhileNotConnectToDB();
	}
	void DisableButtonWhileNotConnectToDB()
	{
		btnSua.setDisable(true);
		btnThem.setDisable(true);
		btnXoa.setDisable(true);
	}
	void EndableButtonWhileConnectToDB()
	{
		btnSua.setDisable(false);
		btnThem.setDisable(false);
		//btnXoa.setDisable(false);
	}
	void MoCSDL()
	{
		System.out.println("Connect to mySQL");
		try {
			connection= DriverManager.getConnection("jdbc:mysql://localhost:3309/QUANLYLICHGIANGDAY", "root", "abcabcabc");
			
			statement = connection.createStatement();	
			System.out.println("Connect succeed!");
			
			LoadCSDL();
			
			EndableButtonWhileConnectToDB();
			
		} catch (SQLException e) {
			
			System.out.println(e.toString());
		}
		
	}
	
	void LoadCSDL()
	{
		data.clear();
		tbvLichGiangDay.getColumns().clear();
	    ResultSet rs;
		try {
			rs = statement.executeQuery("SELECT * FROM LichGiangDay");
			System.out.println(rs.toString());
			ResultSetMetaData rsmd = rs.getMetaData();
			System.out.println("Total columns: " + rsmd.getColumnCount());
			
			for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
                //We are using non property style for making dynamic table
                final int j = i;                
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
                        return new SimpleStringProperty(param.getValue().get(j).toString());                        
                    }                    
                });

                tbvLichGiangDay.getColumns().addAll(col); 
                System.out.println("Column ["+i+"] ");
            }

			while(rs.next()){
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added "+row );
                data.add(row);

            }
			tbvLichGiangDay.setItems(data);
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GetThongKe();
		LoadThongTinThem();

	}
	
	
	void LoadThongTinThem()
	{
		int numClass=0;
		int numGV=0;
		int numEndedClass=0;
		int numLiveClass=0;
		String smallestClass="";
		String largestClass="";
		
		try {
			ResultSetMetaData rsmd=null;
			ResultSet resultSet=null;
			Statement statement= connection.createStatement();
			
			resultSet= statement.executeQuery("select count(STT) FROM lichgiangday");
			while(resultSet.next())
			{
				numClass=resultSet.getInt(1);
			}
			
			resultSet= statement.executeQuery("select count(distinct TenGV) FROM lichgiangday");
			while(resultSet.next())
			{
				numGV=resultSet.getInt(1);
			}
			
			resultSet= statement.executeQuery("select count(STT) FROM lichgiangday WHERE NgayKetThuc>='2018-05-28'");
			while(resultSet.next())
			{
				numLiveClass=resultSet.getInt(1);
			}
			numEndedClass=numClass-numLiveClass;
			
			resultSet= statement.executeQuery("select MaLop From lichgiangday WHERE SiSo= (select max(SiSo) FROM lichgiangday)");
			while(resultSet.next())
			{
				largestClass=resultSet.getString(1);
			}
			
			resultSet= statement.executeQuery("select MaLop From lichgiangday WHERE SiSo= (select min(SiSo) FROM lichgiangday)");
			while(resultSet.next())
			{
				smallestClass=resultSet.getString(1);
			}
			
			
			txtLopItNhat.setText(smallestClass);
			txtLopNhieuNhat.setText(largestClass);
			txtSoGV.setText(numGV+"");
			txtSoLop.setText(numClass+"");
			txtSoLopDienRa.setText(numLiveClass+"");
			txtSoLopKetThuc.setText(numEndedClass+"");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	void LoadDataFromTWToTxt(int index)
	{	
		ObservableList<String> row;
		row= data.get(index);
		dtpNgayStart.setValue(LocalDate.parse(row.get(5)));
		dtpNgayEnd.setValue(LocalDate.parse(row.get(6)));
		txtSTT.setText(row.get(0));
		txtTenMon.setText(row.get(3));
		txtGV.setText(row.get(1));
		txtMaLop.setText(row.get(2));
		txtSiSo.setText(row.get(4));
		txtSoTiet.setText(row.get(7));
		
		
	}
	@FXML
    void HandleTVClick(MouseEvent event) {
		System.out.println("Selected Row:"+ tbvLichGiangDay.getSelectionModel().getSelectedIndex());
		btnXoa.setDisable(false);
		LoadDataFromTWToTxt(tbvLichGiangDay.getSelectionModel().getSelectedIndex());
		
    }
	@FXML
    void HandleBtnThem(ActionEvent event) {
		try {
			String tenGV=txtGV.getText();
			String maLop=txtMaLop.getText();
			String tenMon=txtTenMon.getText();
			int siSo= Integer.parseInt(txtSiSo.getText());
			int soTiet= Integer.parseInt(txtSoTiet.getText());
			String ngayStart = dtpNgayStart.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			String ngayEnd = dtpNgayEnd.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			preparedStatement= connection.prepareStatement("INSERT INTO LichGiangDay (TenGV, MaLop, TenMon, SiSo, NgayBatDau, NgayKetThuc, SoTiet) " + 
					"VALUES (?,?,?,?,?,?,?)");
			preparedStatement.setString(1, tenGV);
			preparedStatement.setString(2, maLop);
			preparedStatement.setString(3, tenMon);
			preparedStatement.setInt(4, siSo);
			preparedStatement.setString(5, ngayStart);
			preparedStatement.setString(6, ngayEnd);
			preparedStatement.setInt(7, soTiet);
			
			preparedStatement.execute();
			
			LoadCSDL();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @FXML
    void HandleBtnXoa(ActionEvent event) {
    	try {
			String Stt=txtSTT.getText();
			preparedStatement= connection.prepareStatement("DELETE FROM lichgiangday WHERE STT=?");
			preparedStatement.setString(1, Stt);
			
			preparedStatement.execute();
			
			LoadCSDL();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @FXML
    void HandleBtnSua(ActionEvent event) {
    	try {
    		String stt=txtSTT.getText();
			String tenGV=txtGV.getText();
			String maLop=txtMaLop.getText();
			String tenMon=txtTenMon.getText();
			int siSo= Integer.parseInt(txtSiSo.getText());
			int soTiet= Integer.parseInt(txtSoTiet.getText());
			String ngayStart = dtpNgayStart.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			String ngayEnd = dtpNgayEnd.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			preparedStatement= connection.prepareStatement("UPDATE lichgiangday SET TenGV=?, MaLop=?, TenMon=?, SiSo=?, NgayBatDau=?, NgayKetThuc=?, SoTiet=? WHERE STT=?");
			preparedStatement.setString(1, tenGV);
			preparedStatement.setString(2, maLop);
			preparedStatement.setString(3, tenMon);
			preparedStatement.setInt(4, siSo);
			preparedStatement.setString(5, ngayStart);
			preparedStatement.setString(6, ngayEnd);
			preparedStatement.setInt(7, soTiet);
			preparedStatement.setString(8, stt);
			
			preparedStatement.execute();
			
			LoadCSDL();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @FXML
    void HandleBtnClear(ActionEvent event) {
    	txtSTT.clear();
    	txtGV.clear();
    	txtMaLop.clear();
    	txtTenMon.clear();
    	txtSoTiet.clear();
    	dtpNgayEnd.getEditor().clear();
    	dtpNgayStart.getEditor().clear();
    	txtSiSo.clear();
    	
    }
    void GetThongKe()
    {	
    	
    	dataCheckGV.clear();
    	tbwThongKe.getColumns().clear();
    	ResultSet rsGV=null;
    	try {
    		Statement GVStament=connection.createStatement();
    		
			rsGV= statement.executeQuery("select distinct TenGV From lichgiangday");
			ResultSet rsSoTietGV=null;
			
			
			TableColumn col1 = new TableColumn("GV");
			TableColumn col2 = new TableColumn("Số tiết");
			TableColumn col3= new TableColumn("Tiêu chuẩn");
			
            col1.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
                public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
                    return new SimpleStringProperty(param.getValue().get(0).toString());                        
                }                    
            });
            col2.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
                public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
                    return new SimpleStringProperty(param.getValue().get(1).toString());                        
                }                    
            });
            col3.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
                public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
                    return new SimpleStringProperty(param.getValue().get(2).toString());                        
                }                    
            });

            tbwThongKe.getColumns().addAll(col1,col2,col3); 
            
            
			while(rsGV.next())
			{	 
				ObservableList<String> row = FXCollections.observableArrayList();
				String nameGV=rsGV.getString(1);
				System.out.println(nameGV);
				row.add(nameGV);
				rsSoTietGV=GVStament.executeQuery("select sum(SoTiet) FROM lichgiangday WHERE TenGV='"+nameGV+"'");
				while(rsSoTietGV.next())
				{	
					int soTiet=rsSoTietGV.getInt(1);
					row.add(rsSoTietGV.getInt(1)+"");
					if(soTiet>=135)
					{
						row.add("Đạt");
					}
					else
					{
						row.add("Không đạt");
					}
				}
				dataCheckGV.add(row);
			}
			tbwThongKe.setItems(dataCheckGV);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    @FXML
    void handleMoCSDL(ActionEvent event) {
    	System.out.println("Mo CSDL");
    	MoCSDL();

    }
	
}
