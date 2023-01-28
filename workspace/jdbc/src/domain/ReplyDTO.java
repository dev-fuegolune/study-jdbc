package domain;
//유저와 댓글 합쳐서 화면에 사용할 클래스
public class ReplyDTO {
	private Long replyCount;//댓글 전체조회시 그룹별로 조회하기 위해서 대댓글의 숫자를 센 replycount를 dto에 만들어줌
	private Long replyId;//pk
	private String replyContent;//댓글내용
	private String replyRegisterDate;//등록 시 시스템 시간
	private String replyUpdateDate;//수정시 시스템 시간
	private Long userId;//댓글fk 댓글을 누르면 댓글 쓴 사람의 정보를 볼수 있도록 조인해서 보냄
	private Long boardId;//게시글fk
	private Long replyGroup;//댓글 그룹. 댓글의 pk인 replyid와 같은 숫자를 써서 구분
	private Long replyDepth;//댓글의 계층 여기서는 대댓글까지 있으므로 1까지만 있다.
	private String userIdentification;//댓글 작성자 아이디
	private String userName;//이름
	private String userPassword;//비번
	private String userPhone;//폰번호
	private String userNickname;//닉네임
	private String userEmail;//이메일
	private String userAddress;//주소
	private String userBirth;//생일
	private String userGender;//성별
	private String userRecommenderId;//추천인 아이디
	
	public ReplyDTO() {;}

	public Long getReplyCount() {
		return replyCount;
	}

	public void setReplyCount(Long replyCount) {
		this.replyCount = replyCount;
	}



	public Long getReplyId() {
		return replyId;
	}

	public void setReplyId(Long replyId) {
		this.replyId = replyId;
	}

	public String getReplyContent() {
		return replyContent;
	}

	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}

	public String getReplyRegisterDate() {
		return replyRegisterDate;
	}

	public void setReplyRegisterDate(String replyRegisterDate) {
		this.replyRegisterDate = replyRegisterDate;
	}

	public String getReplyUpdateDate() {
		return replyUpdateDate;
	}

	public void setReplyUpdateDate(String replyUpdateDate) {
		this.replyUpdateDate = replyUpdateDate;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getBoardId() {
		return boardId;
	}

	public void setBoardId(Long boardId) {
		this.boardId = boardId;
	}

	public Long getReplyGroup() {
		return replyGroup;
	}

	public void setReplyGroup(Long replyGroup) {
		this.replyGroup = replyGroup;
	}

	public Long getReplyDepth() {
		return replyDepth;
	}

	public void setReplyDepth(Long replyDepth) {
		this.replyDepth = replyDepth;
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
	public String toString() {
		return "ReplyDTO [replyCount=" + replyCount + ", replyId=" + replyId + ", replyContent=" + replyContent
				+ ", replyRegisterDate=" + replyRegisterDate + ", replyUpdateDate=" + replyUpdateDate + ", userId="
				+ userId + ", boardId=" + boardId + ", replyGroup=" + replyGroup + ", replyDepth=" + replyDepth
				+ ", userIdentification=" + userIdentification + ", userName=" + userName + ", userPassword="
				+ userPassword + ", userPhone=" + userPhone + ", userNickname=" + userNickname + ", userEmail="
				+ userEmail + ", userAddress=" + userAddress + ", userBirth=" + userBirth + ", userGender=" + userGender
				+ ", userRecommenderId=" + userRecommenderId + "]";
	}
	
	public ReplyVO toReplyVO() {
		ReplyVO replyVO = new ReplyVO();
		replyVO.setReplyId(replyId);
		replyVO.setReplyContent(replyContent);
		replyVO.setUserId(userId);
		replyVO.setBoardId(boardId);
		replyVO.setReplyRegisterDate(replyRegisterDate);
		replyVO.setReplyUpdateDate(replyUpdateDate);
		return replyVO;
	}
}



























