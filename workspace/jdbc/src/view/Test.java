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
//			System.out.println("��� ������ ���̵�");
//			return;
//		}
//		System.out.println("�ߺ��� ���̵�");
//		
//		System.out.println(userDAO.select(1L));
		
		
//		[�ǽ�] �޼ҵ� ��ü ����
//		ȸ������
//		UserVO userVO = new UserVO();
//		userVO.setUserIdentification("lss1234");
//		userVO.setUserName("�̼���");
//		userVO.setUserPassword("1234");
//		userVO.setUserAddress("��⵵ ���ֽ�");
//		userVO.setUserBirth("1545-04-08");
//		userVO.setUserEmail("lss1234@naver.com");
//		userVO.setUserPhone("01099998888");
//		userVO.setUserRecommenderId(null);
//		userVO.setUserIdentification("jbg1234");
//		userVO.setUserName("�庸��");
//		userVO.setUserPassword("9999");
//		userVO.setUserAddress("����Ư���� ���Ǳ�");
//		userVO.setUserBirth("2000-12-04");
//		userVO.setUserEmail("jbg1234@gmail.com");
//		userVO.setUserPhone("01099991234");
//		userVO.setUserRecommenderId("lss1234");
//		userVO.setUserIdentification("phgs1234");
//		userVO.setUserName("�����ż�");
//		userVO.setUserPassword("7890");
//		userVO.setUserAddress("������ ������");
//		userVO.setUserBirth("2010-12-04");
//		userVO.setUserEmail("phgs1234@gmail.com");
//		userVO.setUserPhone("01032291234");
//		userVO.setUserRecommenderId("lss1234");
//		�Ű������� �Ѱ��� userVO��ü�� ������ ����
//		if(userDAO.checkId(userVO.getUserIdentification())) {//ȸ�������� ���̵� �ߺ��˻��ϴϱ� �ߺ��˻� �޼ҵ� ���� �Ἥ true �� �ߺ����̵� ���� ��쿡��
//			userDAO.insert(userVO);//ȸ������ ����
//		}else {
//			System.out.println("�ߺ��� ���̵�");
//		}
		
//		�α���
//		if(userDAO.login("lss1234", "1234")) {
//			System.out.println("�α��� ����");
//			System.out.println(UserDAO.userId);//pk�� Ȯ������ ���
//		}else {
//			System.out.println("�α��� ����");
//		}
		
//		���̵� ã��
//		System.out.println(userDAO.findIdentification("01099991234"));
		
//		��й�ȣ ����
//		userDAO.updateUserPassword("3333");//�������
		
//		if(userDAO.login("lss1234", "3333")) {//������ ����� ���̵�� �α��� �Ǵ��� Ȯ��
//			System.out.println("�α��� ����");
//			System.out.println(UserDAO.userId);
//		}else {
//			System.out.println("�α��� ����");
//		}
		
//		ȸ������ ��ȸ
//		UserVO user = userDAO.select();
		
//		ȸ������ ����
//		user.setUserNickname("�źϿ�");//������ ���� userVO�� �����. �������� �״�� ������Ʈ
//		userDAO.update(user);
		
//		��õ ��
//		System.out.println(userDAO.getCountOfRecommenderId("jbg1234"));
		
//		���� ��õ�� ���
//		userDAO.selectRecommenders().stream().map(UserVO::toString).forEach(System.out::println);
		
//		���� ��õ�� ���
		
//		if(userDAO.login("jbg1234", "9999")) {
//			System.out.println("�α��� ����");
//			System.out.println(UserDAO.userId);
//		}else {
//			System.out.println("�α��� ����");
//		}
		
//		UserVO user = userDAO.getMyRecommender();
//		if(user != null) {
//			System.out.println(user);
//			
//		}else {
//			System.out.println("��õ�� ����� �����ϴ�.");
//		}
		
//		ȸ�� Ż��
//		userDAO.delete();
//		==========================================================================
//		BoardDAO boardDAO = new BoardDAO(); �޼ҵ� ����� ���ؼ� DAO��ü����
		
//		�Խñ� �߰�
//		BoardVO boardVO = new BoardVO(); 
//		
//		if(userDAO.login("phgs1234", "7890")) {//�α��� ����
//			System.out.println("�α��� ����");
//			System.out.println(UserDAO.userId);
//		}else {
//			System.out.println("�α��� ����");
//		}
//		
//		boardVO.setBoardTitle("�˿��� �¾");//�Խñ� VO�� ���� ä����
//		boardVO.setBoardContent("������ �����ϴ�.");
//		boardVO.setUserId(UserDAO.userId);//�α��� ���¿��� �۾��� ������ userDAO�� static������ userId���
//		
//		boardDAO.insert(boardVO);//���
		
//		�Խñ� ��ȸ
//		System.out.println(boardDAO.select(1L));//�Խñ� pk�� �Խñ� ��ȸ�� syso �ؼ� Ȯ��
//		BoardDTO boardDTO = boardDAO.select(2L);
		
//		�Խñ� ����
//		if(UserDAO.userId == boardDTO.getUserId()) {//�����̾���� ���������ϴϱ� 
//			boardDTO.setBoardTitle("������ ����");//������ ���� DTO�� ������
//			boardDAO.update(boardDTO.toBoardVO());//�Ű�������VO�̹Ƿ� DTO�� VO�� �ٲ��� �޼ҵ� ���� ���
//		}else {
//			System.out.println("�ۼ��ڸ� ������ �����մϴ�.");
//		}
		
//		�Խñ� ����
//		if(UserDAO.userId == boardDTO.getUserId()) {//������ ����
//			boardDAO.delete(boardDTO.getBoardId());
//		}else {
//			System.out.println("�ۼ��ڸ� ������ �����մϴ�.");
//		}
		
//		�Խñ� ��ü ��ȸ
//		boardDAO.selectAll().stream().map(BoardDTO::toString).forEach(System.out::println);
//		Arraylist�̹Ƿ� Stream���� �����ϰ� Ȯ��.  
//		==========================================================================
		UserDAO userDAO = new UserDAO();
		BoardDAO boardDAO = new BoardDAO();
		ReplyDAO replyDAO = new ReplyDAO();
//		
		if(userDAO.login("phgs1234", "7890")) {
			System.out.println("�α��� ����");
		}else {
			System.out.println("�α��� ����");
		}
		
//		BoardDTO boardDTO = boardDAO.select(3L);
//		System.out.println(boardDTO);
//		
//		//(��� �߰�)�α��� ���¿��� ���ǹǷ� �α��θ޼ҵ嵵 ���� �ּ� ������ ���
//		ReplyVO replyVO = new ReplyVO();
//		replyVO.setBoardId(boardDTO.getBoardId());
//		replyVO.setReplyContent("�ι�° ���");
//		
//		replyDAO.insert(replyVO);
//		System.out.println(replyVO);
//		(���� �߰�) �α��� ���¿��� ���ǹǷ� �α��θ޼ҵ嵵 ���� �ּ� ������ ���
//		ReplyVO replyVO = new ReplyVO();
//		replyVO.setBoardId(boardDTO.getBoardId());
//		replyVO.setReplyContent("ù��° ����� ù��° ����");
//		replyVO.setReplyContent("ù��° ����� �ι�° ����");
//		replyVO.setReplyContent("�ι�° ����� ù��° ����");
//		
//		replyDAO.insert(replyVO, 5L);
//		
//		��� ��ü ��ȸ
//		��� �׷��� 1�� �ֵ鸸 ��ȸ�� ��� 
//		replyDAO.selectAll().stream().filter(dto -> dto.getReplyGroup() == 1).map(ReplyDTO::toString).forEach(System.out::println);
//		ArrayList�̹Ƿ� �ϳ��� stream ������� �̾ƿͼ� ����ϸ� ��~DTO�� �Ű������� �޾Ƽ� ��� �׷��� 1�� ��۵鸸 �̾Ƽ�(filter)replyDTO�� 
//		map���� ����(filter���� �ɸ� ����� dto�� �Ѱ��༭ ���ٽ����� ��� dto->dto.toString => ()�ȿ� dto�� �ȵ��� Ŭ������ ����. �ϳ��� ���
//		replyDAO.selectAll().stream().filter(dto -> dto.getReplyGroup() == 5).map(ReplyDTO::toString).forEach(System.out::println);
//		��ȸ�� ��Ʈ���� ����Ͽ� ��� �׷캰�� ��ȸ. ��ư�� ������ 5�� ��� �׷��ξֵ� ȭ�鿡 �Ѹ��� �ְ� ����
//		���� ����
//		replyDAO.deleteReReply(4L);
		
//		��� ����
//		replyDAO.deleteReply(5L);
		
//		��� ����																	DTOŸ���� replyDTO.toReplyVO�޼ҵ带 ����Ͽ� replyVO�� �ٲ���(�������� �Ű������� VOŸ���� ��)
//����� ��ȸ�� ��������.��ȸ �޼ҵ带 ���ؼ� �����ϰ��� �ϴ� ����� �����µ� select��DTOŸ���̹Ƿ� VO�ιٲ���
//		replyDAO.selectAll().stream().filter(dto -> dto.getReplyId() == 5).map(ReplyDTO::toReplyVO).forEach(vo -> {
//			vo.setReplyContent("������ ���"); ���������� �����ص� ������Ʈ �޼ҵ� ���
//			replyDAO.update(vo);
//		});
		
		
	}
}































