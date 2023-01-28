package view;

import dao.BoardDAO;
import dao.ReplyDAO;
import dao.UserDAO;
import domain.BoardDTO;
import domain.BoardVO;
import domain.ReplyDTO;
import domain.ReplyVO;
import domain.UserVO;

public class Test {
	public static void main(String[] args) {
//		UserDAO userDAO = new UserDAO(); 
//		if(userDAO.checkId("hds1234")) {
//			System.out.println("사용 가능한 아이디");
//			return;
//		}
//		System.out.println("중복된 아이디");
//		
//		System.out.println(userDAO.select(1L));
		
		
//		[실습] 메소드 전체 검증
//		회원가입
//		UserVO userVO = new UserVO();
//		userVO.setUserIdentification("lss1234");
//		userVO.setUserName("이순신");
//		userVO.setUserPassword("1234");
//		userVO.setUserAddress("경기도 파주시");
//		userVO.setUserBirth("1545-04-08");
//		userVO.setUserEmail("lss1234@naver.com");
//		userVO.setUserPhone("01099998888");
//		userVO.setUserRecommenderId(null);
//		userVO.setUserIdentification("jbg1234");
//		userVO.setUserName("장보고");
//		userVO.setUserPassword("9999");
//		userVO.setUserAddress("서울특별시 관악구");
//		userVO.setUserBirth("2000-12-04");
//		userVO.setUserEmail("jbg1234@gmail.com");
//		userVO.setUserPhone("01099991234");
//		userVO.setUserRecommenderId("lss1234");
//		userVO.setUserIdentification("phgs1234");
//		userVO.setUserName("박혁거세");
//		userVO.setUserPassword("7890");
//		userVO.setUserAddress("강원도 강릉시");
//		userVO.setUserBirth("2010-12-04");
//		userVO.setUserEmail("phgs1234@gmail.com");
//		userVO.setUserPhone("01032291234");
//		userVO.setUserRecommenderId("lss1234");
//		매개변수로 넘겨줄 userVO객체에 정보를 세팅
//		if(userDAO.checkId(userVO.getUserIdentification())) {//회원가입전 아이디 중복검사하니까 중복검사 메소드 먼저 써서 true 즉 중복아이디가 없을 경우에만
//			userDAO.insert(userVO);//회원가입 실행
//		}else {
//			System.out.println("중복된 아이디");
//		}
		
//		로그인
//		if(userDAO.login("lss1234", "1234")) {
//			System.out.println("로그인 성공");
//			System.out.println(UserDAO.userId);//pk값 확인위해 출력
//		}else {
//			System.out.println("로그인 실패");
//		}
		
//		아이디 찾기
//		System.out.println(userDAO.findIdentification("01099991234"));
		
//		비밀번호 변경
//		userDAO.updateUserPassword("3333");//비번변경
		
//		if(userDAO.login("lss1234", "3333")) {//변경후 변경된 아이디로 로그인 되는지 확인
//			System.out.println("로그인 성공");
//			System.out.println(UserDAO.userId);
//		}else {
//			System.out.println("로그인 실패");
//		}
		
//		회원정보 조회
//		UserVO user = userDAO.select();
		
//		회원정보 수정
//		user.setUserNickname("거북왕");//수정할 값만 userVO에 담아줌. 나머지는 그대로 업데이트
//		userDAO.update(user);
		
//		추천 수
//		System.out.println(userDAO.getCountOfRecommenderId("jbg1234"));
		
//		나를 추천한 사람
//		userDAO.selectRecommenders().stream().map(UserVO::toString).forEach(System.out::println);
		
//		내가 추천한 사람
		
//		if(userDAO.login("jbg1234", "9999")) {
//			System.out.println("로그인 성공");
//			System.out.println(UserDAO.userId);
//		}else {
//			System.out.println("로그인 실패");
//		}
		
//		UserVO user = userDAO.getMyRecommender();
//		if(user != null) {
//			System.out.println(user);
//			
//		}else {
//			System.out.println("추천한 사람이 없습니다.");
//		}
		
//		회원 탈퇴
//		userDAO.delete();
//		==========================================================================
//		BoardDAO boardDAO = new BoardDAO(); 메소드 사용을 위해서 DAO객체생성
		
//		게시글 추가
//		BoardVO boardVO = new BoardVO(); 
//		
//		if(userDAO.login("phgs1234", "7890")) {//로그인 먼저
//			System.out.println("로그인 성공");
//			System.out.println(UserDAO.userId);
//		}else {
//			System.out.println("로그인 실패");
//		}
//		
//		boardVO.setBoardTitle("알에서 태어나");//게시글 VO에 정보 채워서
//		boardVO.setBoardContent("왕으로 강림하다.");
//		boardVO.setUserId(UserDAO.userId);//로그인 상태에서 글쓰기 때문에 userDAO의 static변수인 userId사용
//		
//		boardDAO.insert(boardVO);//사용
		
//		게시글 조회
//		System.out.println(boardDAO.select(1L));//게시글 pk로 게시글 조회후 syso 해서 확인
//		BoardDTO boardDTO = boardDAO.select(2L);
		
//		게시글 수정
//		if(UserDAO.userId == boardDTO.getUserId()) {//본인이어야지 수정가능하니까 
//			boardDTO.setBoardTitle("수정된 제목");//수정할 내용 DTO에 세팅후
//			boardDAO.update(boardDTO.toBoardVO());//매개변수가VO이므로 DTO에 VO로 바꿔줄 메소드 만들어서 사용
//		}else {
//			System.out.println("작성자만 수정이 가능합니다.");
//		}
		
//		게시글 삭제
//		if(UserDAO.userId == boardDTO.getUserId()) {//수정과 같음
//			boardDAO.delete(boardDTO.getBoardId());
//		}else {
//			System.out.println("작성자만 삭제가 가능합니다.");
//		}
		
//		게시글 전체 조회
//		boardDAO.selectAll().stream().map(BoardDTO::toString).forEach(System.out::println);
//		Arraylist이므로 Stream으로 간단하게 확인.  
//		==========================================================================
		UserDAO userDAO = new UserDAO();
		BoardDAO boardDAO = new BoardDAO();
		ReplyDAO replyDAO = new ReplyDAO();
//		
		if(userDAO.login("phgs1234", "7890")) {
			System.out.println("로그인 성공");
		}else {
			System.out.println("로그인 실패");
		}
		
//		BoardDTO boardDTO = boardDAO.select(3L);
//		System.out.println(boardDTO);
//		
//		//(댓글 추가)로그인 상태에서 사용되므로 로그인메소드도 같이 주석 제거후 사용
//		ReplyVO replyVO = new ReplyVO();
//		replyVO.setBoardId(boardDTO.getBoardId());
//		replyVO.setReplyContent("두번째 댓글");
//		
//		replyDAO.insert(replyVO);
//		System.out.println(replyVO);
//		(대댓글 추가) 로그인 상태에서 사용되므로 로그인메소드도 같이 주석 제거후 사용
//		ReplyVO replyVO = new ReplyVO();
//		replyVO.setBoardId(boardDTO.getBoardId());
//		replyVO.setReplyContent("첫번째 댓글의 첫번째 대댓글");
//		replyVO.setReplyContent("첫번째 댓글의 두번째 대댓글");
//		replyVO.setReplyContent("두번째 댓글의 첫번째 대댓글");
//		
//		replyDAO.insert(replyVO, 5L);
//		
//		댓글 전체 조회
//		댓글 그룹이 1인 애들만 조회후 출력 
//		replyDAO.selectAll().stream().filter(dto -> dto.getReplyGroup() == 1).map(ReplyDTO::toString).forEach(System.out::println);
//		ArrayList이므로 하나씩 stream 방식으로 뽑아와서 사용하면 편리~DTO를 매개변수로 받아서 댓글 그룹이 1인 댓글들만 뽑아서(filter)replyDTO를 
//		map으로 가공(filter에서 걸린 결과값 dto를 넘겨줘서 람다식으로 사용 dto->dto.toString => ()안에 dto가 안들어가니 클래스로 줄임. 하나씩 출력
//		replyDAO.selectAll().stream().filter(dto -> dto.getReplyGroup() == 5).map(ReplyDTO::toString).forEach(System.out::println);
//		조회시 스트림을 사용하여 댓글 그룹별로 조회. 버튼을 누르면 5번 댓글 그룹인애들 화면에 뿌릴수 있게 설계
//		대댓글 삭제
//		replyDAO.deleteReReply(4L);
		
//		댓글 삭제
//		replyDAO.deleteReply(5L);
		
//		댓글 수정																	DTO타입을 replyDTO.toReplyVO메소드를 사용하여 replyVO로 바꿔줌(수정에는 매개변수로 VO타입이 들어감)
//댓글은 조회후 수정가능.조회 메소드를 통해서 수정하고자 하는 댓글을 가져온뒤 select는DTO타입이므로 VO로바꿔줌
//		replyDAO.selectAll().stream().filter(dto -> dto.getReplyId() == 5).map(ReplyDTO::toReplyVO).forEach(vo -> {
//			vo.setReplyContent("수정된 댓글"); 수정내용을 적어준뒤 업데이트 메소드 사용
//			replyDAO.update(vo);
//		});
		
		
	}
}































