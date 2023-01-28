package domain;

public class UserVO {
	private Long userId;//pk값으로 사용할 userid변수 Long으로 사용한 이유는 홈페이지가 언제까지 지속될지 모르니 긴long타입. 클래스로 사용한 이유는 없으면null 값으로 리턴하기 위해
	private String userIdentification;//유저의 id pk와 구분을 위해identification을 사용한다. 
	private String userName;//유저 이름
	private String userPassword;//유저 비번
	private String userPhone;//유저 폰번호
	private String userNickname;//유저닉네임
	private String userEmail;//유저 이메일
	private String userAddress;//유저 주소
	private String userBirth;//유저 생일
	private String userGender;//유저 성별
	private String userRecommenderId;//유저가 추천한 아이디 
//	변수는 테이블 행을 바탕으로 만듦
//	private Long recommendCount; //행분리를 안했을 경우에 userVO에 추천인수 변수를 넣어줌
	
	public UserVO() {;}
//기본 생성자
	public Long getUserId() {
		return userId;
	}//private이므로 getter setter를 통해서 변수사용

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserIdentification() {
		return userIdentification;
	}

	public void setUserIdentification(String userIdentification) {
		this.userIdentification = userIdentification;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserNickname() {
		return userNickname;
	}

	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public String getUserBirth() {
		return userBirth;
	}

	public void setUserBirth(String userBirth) {
		this.userBirth = userBirth;
	}

	public String getUserGender() {
		return userGender;
	}

	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}

	public String getUserRecommenderId() {
		return userRecommenderId;
	}

	public void setUserRecommenderId(String userRecommenderId) {
		this.userRecommenderId = userRecommenderId;
	}

	@Override
	public String toString() {//toString 재정의
		return "UserVO [userId=" + userId + ", userIdentification=" + userIdentification + ", userName=" + userName
				+ ", userPassword=" + userPassword + ", userPhone=" + userPhone + ", userNickname=" + userNickname
				+ ", userEmail=" + userEmail + ", userAddress=" + userAddress + ", userBirth=" + userBirth
				+ ", userGender=" + userGender + ", userRecommenderId=" + userRecommenderId + "]";
	}

//	public Long getRecommendCount() {//분리안했을경우 
//		return recommendCount;
//	}
//
//	public void setRecommendCount(Long recommendCount) {
//		this.recommendCount = recommendCount;
//	}

//	@Override
//	public String toString() {
//		return "UserVO [userId=" + userId + ", userIdentification=" + userIdentification + ", userName=" + userName
//				+ ", userPassword=" + userPassword + ", userPhone=" + userPhone + ", userNickname=" + userNickname
//				+ ", userEmail=" + userEmail + ", userAddress=" + userAddress + ", userBirth=" + userBirth
//				+ ", userGender=" + userGender + ", userRecommenderId=" + userRecommenderId + ", recommendCount="
//				+ recommendCount + "]";
//	}
}
