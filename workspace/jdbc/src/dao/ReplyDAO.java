package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import domain.ReplyDTO;
import domain.ReplyVO;

public class ReplyDAO {
	public Connection connection;
	public PreparedStatement preparedStatement;
	public ResultSet resultSet;
	
//	대댓글 추가							
	public void insert(ReplyVO replyVO, Long target) {
		//매개 변수-추가할 대댓글의 내용 전체를 replyVO객체로 받아오고 reply group과 reply depth를 알아내기 위해서 댓글의 replyid=groupid를 매개변수로 받아온다 
		String query = "INSERT INTO TBL_REPLY"
				+ "(REPLY_ID, REPLY_CONTENT, USER_ID, BOARD_ID, REPLY_GROUP, REPLY_DEPTH) "
				+ "VALUES(SEQ_REPLY.NEXTVAL, ?, ?, ?, ?, (SELECT REPLY_DEPTH + 1 FROM TBL_REPLY WHERE REPLY_ID = ?))";
//		PK값 시퀀스로 안겹치게 설정, USERVO에서 댓글내용 가져옴, 로그인 상태이니까 USERID는 세션에 저장된 로그인 아이디를 가져옴, 게시글 아이디는 REPLYVO에서 GETTER를 사용하여
//		받아옴. REPLYGROUP과 REPLY DEPTH는 REPLYVO에 정보가 없으므로 따로 매개변수로 댓글의 그룹 아이디 즉 REPLY_ID(LONG) 받아온걸로 댓글의 정보가 있는 열을 찾아서 댓글을 가져와+1을 한 
//		값을 DEPTH로 사용한다

		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, replyVO.getReplyContent());
			preparedStatement.setLong(2, UserDAO.userId);//로그인 상태라고 가정하고 userid를 static변수로 받아옴 
			preparedStatement.setLong(3, replyVO.getBoardId());
			preparedStatement.setLong(4, target);//원 댓글의 replyid가 replygroup 이므로 매개변수로 받아온 댓글의 groupid를 넣어줌
			preparedStatement.setLong(5, target);//매개변수로 받은 reply group(target)으로 달 댓글의 행을 찾아 댓글의 depth에 1을 더해서 대댓글로 만들어줌 
			
			preparedStatement.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(preparedStatement != null) {
					preparedStatement.close();
				}
				if(connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		
	}
	
//	댓글 추가	댓글은 DEPTH가 0이므로 오버로딩을 통해 따로 메소드를 하나더 만들어 사용 						
	public void insert(ReplyVO replyVO) {//replyVO객체를 받아와서 getter를 사용하여 ?에 넣어주기 위해 매개변수로 사용
//	대댓글 추가가 아니라 댓글 추가이므로 
		String query = "INSERT INTO TBL_REPLY"
				+ "(REPLY_ID, REPLY_CONTENT, USER_ID, BOARD_ID, REPLY_GROUP, REPLY_DEPTH) "
				+ "VALUES(SEQ_REPLY.NEXTVAL, ?, ?, ?, SEQ_REPLY.CURRVAL, 0)";
//	pk로 사용할 replyid는 중복값이 없어야하니 시퀀스로 사용,댓글 내용,게시판아이디는 replyVO를 통해 넣어줌
//	replygroup은 중복이 없어야함과 동시에 다른 댓글과 구분하여 대댓글까지 묶어야하므로 reply_Id와 같은 값을 사용-> .CURRVAL를 사용하여 현재의 시퀀스를 가져온다
//			 	0=>대댓글이 아닌 댓글이라서 계층이 없으므로 0으로 사용
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);//connection을 객체를 통해서 쿼리전달
			preparedStatement.setString(1, replyVO.getReplyContent());
			preparedStatement.setLong(2, UserDAO.userId);//로그인상태에서 댓글을 쓰므로 세션에 있는 userid를 사용
			preparedStatement.setLong(3, replyVO.getBoardId());
//											
			preparedStatement.executeUpdate();//쿼리 실행하여 성공한 개수를 int 타입으로 리턴. 
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(preparedStatement != null) {
					preparedStatement.close();
				}
				if(connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
//	댓글 전체 조회
	public ArrayList<ReplyDTO> selectAll(){
//					링크를 통해서 작성자 정보를 조회할수 있도록 dto를 통해서 댓글 정보와 작성자 정보를 묶어서 클래스를 만들어 보내준다 전제 댓글 조회이고 몇개까지 있을지 모르니까 ArrayList사용		 
//		작성자 테이블과 댓글 테이블 정보 모두를 가져와야하므로 join사용
//				대댓글의 경우 null 값 대신 0을 넣어줌/   대댓글 수를 같이 담아줌 ->그룹별로 조회가 가능하도록
//R1=댓글 테이블에서 같은 그룹별로 묶어서 그룹에 속한 댓글의 숫자를 댓글마다 다른 pk 즉 replyid를 통해서 구해준다. 여기서 원댓글도 같은 그룹으로 count 안에 포함되므로 -1해줌
		String query = "SELECT NVL(REPLY_COUNT, 0) REPLY_COUNT, REPLY_ID, REPLY_CONTENT, R2.USER_ID, BOARD_ID, REPLY_REGISTER_DATE, REPLY_UPDATE_DATE, " 
				+ "R2.REPLY_GROUP, REPLY_DEPTH, " 
				+ "USER_IDENTIFICATION, USER_NAME, USER_PASSWORD, " 
				+ "USER_PHONE, USER_NICKNAME, USER_EMAIL, USER_ADDRESS, USER_BIRTH, " 
				+ "USER_GENDER, USER_RECOMMENDER_ID " 
				+ "FROM (SELECT REPLY_GROUP, COUNT(REPLY_ID) - 1 REPLY_COUNT FROM TBL_REPLY GROUP BY REPLY_GROUP) R1 "
				+ "RIGHT OUTER JOIN VIEW_REPLY_USER R2 "
				+ "ON R1.REPLY_GROUP = R2.REPLY_GROUP AND R1.REPLY_GROUP = R2.REPLY_ID";
//				
//					R2 = replyDTO 즉 유저정보와 댓글 정보를 합친 테이블을 뷰로 만들어 사용 		
//					다만 아래의 조건식이 대댓글이 아니라 댓글만 뽑아오게 되므로(그룹아이디와 댓글아이디가 같은조건)right outer를 사용하여 대댓글도 뽑아옴  
		ReplyDTO replyDTO = null;//결과값을 넣어줄 replyDTO를 만들어주고 null을 넣어줌 결과값이 없으면 null로 돌려주기 위해
		int index = 0;//변수 차례 숫자 넣기 귀찮으니까 index++해주기 위해 만듦
		ArrayList<ReplyDTO> replies = new ArrayList<ReplyDTO>();
		//여러개의 replyDTO를 담기위한 ArrayList 만듦.조회하고자 하는 댓글이 몇개일지 모르니 ArrayList로 만든다. 
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);//쿼리전달
			resultSet = preparedStatement.executeQuery();//쿼리 실행후 결과값을 resultSet에 담아준다
			
			while(resultSet.next()) {//값을 하나씩 뽑아옴.ArrayList쓴것과 같은 이유로 while사용
				index = 0;//인덱스 한번 돌때마다 초기화.
				replyDTO = new ReplyDTO();
				replyDTO.setReplyCount(resultSet.getLong(++index));//replyDTO에 변수 만든후 setter를 통해 넣어줌
				replyDTO.setReplyId(resultSet.getLong(++index));
				replyDTO.setReplyContent(resultSet.getString(++index));
				replyDTO.setUserId(resultSet.getLong(++index));
				replyDTO.setBoardId(resultSet.getLong(++index));
				replyDTO.setReplyRegisterDate(resultSet.getString(++index));
				replyDTO.setReplyUpdateDate(resultSet.getString(++index));
				replyDTO.setReplyGroup(resultSet.getLong(++index));
				replyDTO.setReplyDepth(resultSet.getLong(++index));
				replyDTO.setUserIdentification(resultSet.getString(++index));
				replyDTO.setUserName(resultSet.getString(++index));
				replyDTO.setUserPassword(resultSet.getString(++index));
				replyDTO.setUserPhone(resultSet.getString(++index));
				replyDTO.setUserNickname(resultSet.getString(++index));
				replyDTO.setUserEmail(resultSet.getString(++index));
				replyDTO.setUserAddress(resultSet.getString(++index));
				replyDTO.setUserBirth(resultSet.getString(++index));
				replyDTO.setUserGender(resultSet.getString(++index));
				replyDTO.setUserRecommenderId(resultSet.getString(++index));
				replies.add(replyDTO);//모든 변수를 넣은후 ArrayList에 넣어준다
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(preparedStatement != null) {
					preparedStatement.close();
				}
				if(connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return replies;//결과 ArrayList 리턴
	}

//	대댓글 삭제
	public void deleteReReply(Long replyId) {//pk를받아와서 
		String query = "DELETE FROM TBL_REPLY WHERE REPLY_ID = ?";//삭제하고자 하는 행을 찾아서 delete문 실행
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, replyId);//매개변수로 받은 pk를 ?에 넣어준다
			preparedStatement.executeUpdate();//실행하면 executeUpdate가 성공 횟수를 int값으로 돌려줌. 이경우는 reply id를 하나 넣어서 실행하므로 0아니면 1
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(preparedStatement != null) {
					preparedStatement.close();
				}
				if(connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
//	댓글 삭제			
	public void deleteReply(Long groupId) {//댓글이 삭제되면 대댓글도 삭제되어야 참조 무결성이 유지되므로 댓글그룹을 매개변수로 써서 싹다 지워줌
		String query = "DELETE FROM TBL_REPLY WHERE REPLY_GROUP = ?";//같은 댓글 그룹을 가진 행(댓글-대댓글)을 모두 찾은후 삭제
	
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, groupId);
			System.out.println(preparedStatement.executeUpdate());//실행 이경우는 replycount+1만큼 결과가 나옴
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(preparedStatement != null) {
					preparedStatement.close();
				}
				if(connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
//	댓글 수정
	public void update(ReplyVO replyVO) {//수정하고자 하는 댓글 정보를 replyVO로 담아 매개변수로 전달
		String query = "UPDATE TBL_REPLY SET REPLY_CONTENT = ?, REPLY_UPDATE_DATE = SYSDATE WHERE REPLY_ID = ?";
//											내용수정			수정하니까 메소드를 실행하면 update date를 시스템 시간으로 세팅 pk로 행찾음
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, replyVO.getReplyContent());//첫번째 ?에 들어갈 변수를 replyVO에서 getter이용 넣어줌 
			preparedStatement.setLong(2, replyVO.getReplyId());//두번째 물음표에 들어갈 변수를 replyVO에서 가져옴
			preparedStatement.executeUpdate();//실행
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(preparedStatement != null) {
					preparedStatement.close();
				}
				if(connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}
}































