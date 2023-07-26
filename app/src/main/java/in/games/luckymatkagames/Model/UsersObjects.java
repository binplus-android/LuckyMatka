package in.games.luckymatkagames.Model;

public class UsersObjects {

    String id,name,username,mobileno,email,address,city,pincode,password,accountno,bank_name,ifsc_code,account_holder_name,paytm_no,tez_no,phonepay_no ,dob;

    public UsersObjects() {
    }

    public UsersObjects(String id, String name, String username, String mobileno, String email, String address, String city, String pincode, String password, String accountno, String bank_name, String ifsc_code, String account_holder_name, String paytm_no, String tez_no, String phonepay_no, String dob) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.mobileno = mobileno;
        this.email = email;
        this.address = address;
        this.city = city;
        this.pincode = pincode;
        this.password = password;
        this.accountno = accountno;
        this.bank_name = bank_name;
        this.ifsc_code = ifsc_code;
        this.account_holder_name = account_holder_name;
        this.paytm_no = paytm_no;
        this.tez_no = tez_no;
        this.phonepay_no = phonepay_no;
        this.dob = dob;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccountno() {
        return accountno;
    }

    public void setAccountno(String accountno) {
        this.accountno = accountno;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getIfsc_code() {
        return ifsc_code;
    }

    public void setIfsc_code(String ifsc_code) {
        this.ifsc_code = ifsc_code;
    }

    public String getAccount_holder_name() {
        return account_holder_name;
    }

    public void setAccount_holder_name(String account_holder_name) {
        this.account_holder_name = account_holder_name;
    }

    public String getPaytm_no() {
        return paytm_no;
    }

    public void setPaytm_no(String paytm_no) {
        this.paytm_no = paytm_no;
    }

    public String getTez_no() {
        return tez_no;
    }

    public void setTez_no(String tez_no) {
        this.tez_no = tez_no;
    }

    public String getPhonepay_no() {
        return phonepay_no;
    }

    public void setPhonepay_no(String phonepay_no) {
        this.phonepay_no = phonepay_no;
    }
}
