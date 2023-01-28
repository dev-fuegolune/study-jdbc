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
	
//	���� �߰�							
	public void insert(ReplyVO replyVO, Long target) {
		//�Ű� ����-�߰��� ������ ���� ��ü�� replyVO��ü�� �޾ƿ��� reply group�� reply depth�� �˾Ƴ��� ���ؼ� ����� replyid=groupid�� �Ű������� �޾ƿ´� 
		String query = "INSERT INTO TBL_REPLY"
				+ "(REPLY_ID, REPLY_CONTENT, USER_ID, BOARD_ID, REPLY_GROUP, REPLY_DEPTH) "
				+ "VALUES(SEQ_REPLY.NEXTVAL, ?, ?, ?, ?, (SELECT REPLY_DEPTH + 1 FROM TBL_REPLY WHERE REPLY_ID = ?))";
//		PK�� �������� �Ȱ�ġ�� ����, USERVO���� ��۳��� ������, �α��� �����̴ϱ� USERID�� ���ǿ� ����� �α��� ���̵� ������, �Խñ� ���̵�� REPLYVO���� GETTER�� ����Ͽ�
//		�޾ƿ�. REPLYGROUP�� REPLY DEPTH�� REPLYVO�� ������ �����Ƿ� ���� �Ű������� ����� �׷� ���̵� �� REPLY_ID(LONG) �޾ƿ°ɷ� ����� ������ �ִ� ���� ã�Ƽ� ����� ������+1�� �� 
//		���� DEPTH�� ����Ѵ�

		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, replyVO.getReplyContent());
			preparedStatement.setLong(2, UserDAO.userId);//�α��� ���¶�� �����ϰ� userid�� static������ �޾ƿ� 
			preparedStatement.setLong(3, replyVO.getBoardId());
			preparedStatement.setLong(4, target);//�� ����� replyid�� replygroup �̹Ƿ� �Ű������� �޾ƿ� ����� groupid�� �־���
			preparedStatement.setLong(5, target);//�Ű������� ���� reply group(target)���� �� ����� ���� ã�� ����� depth�� 1�� ���ؼ� ���۷� ������� 
			
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
	
//	��� �߰�	����� DEPTH�� 0�̹Ƿ� �����ε��� ���� ���� �޼ҵ带 �ϳ��� ����� ��� 						
	public void insert(ReplyVO replyVO) {//replyVO��ü�� �޾ƿͼ� getter�� ����Ͽ� ?�� �־��ֱ� ���� �Ű������� ���
//	���� �߰��� �ƴ϶� ��� �߰��̹Ƿ� 
		String query = "INSERT INTO TBL_REPLY"
				+ "(REPLY_ID, REPLY_CONTENT, USER_ID, BOARD_ID, REPLY_GROUP, REPLY_DEPTH) "
				+ "VALUES(SEQ_REPLY.NEXTVAL, ?, ?, ?, SEQ_REPLY.CURRVAL, 0)";
//	pk�� ����� replyid�� �ߺ����� ������ϴ� �������� ���,��� ����,�Խ��Ǿ��̵�� replyVO�� ���� �־���
//	replygroup�� �ߺ��� ������԰� ���ÿ� �ٸ� ��۰� �����Ͽ� ���۱��� ������ϹǷ� reply_Id�� ���� ���� ���-> .CURRVAL�� ����Ͽ� ������ �������� �����´�
//			 	0=>������ �ƴ� ����̶� ������ �����Ƿ� 0���� ���
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);//connection�� ��ü�� ���ؼ� ��������
			preparedStatement.setString(1, replyVO.getReplyContent());
			preparedStatement.setLong(2, UserDAO.userId);//�α��λ��¿��� ����� ���Ƿ� ���ǿ� �ִ� userid�� ���
			preparedStatement.setLong(3, replyVO.getBoardId());
//											
			preparedStatement.executeUpdate();//���� �����Ͽ� ������ ������ int Ÿ������ ����. 
			
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
	
//	��� ��ü ��ȸ
	public ArrayList<ReplyDTO> selectAll(){
//					��ũ�� ���ؼ� �ۼ��� ������ ��ȸ�Ҽ� �ֵ��� dto�� ���ؼ� ��� ������ �ۼ��� ������ ��� Ŭ������ ����� �����ش� ���� ��� ��ȸ�̰� ����� ������ �𸣴ϱ� ArrayList���		 
//		�ۼ��� ���̺�� ��� ���̺� ���� ��θ� �����;��ϹǷ� join���
//				������ ��� null �� ��� 0�� �־���/   ���� ���� ���� ����� ->�׷캰�� ��ȸ�� �����ϵ���
//R1=��� ���̺��� ���� �׷캰�� ��� �׷쿡 ���� ����� ���ڸ� ��۸��� �ٸ� pk �� replyid�� ���ؼ� �����ش�. ���⼭ ����۵� ���� �׷����� count �ȿ� ���ԵǹǷ� -1����
		String query = "SELECT NVL(REPLY_COUNT, 0) REPLY_COUNT, REPLY_ID, REPLY_CONTENT, R2.USER_ID, BOARD_ID, REPLY_REGISTER_DATE, REPLY_UPDATE_DATE, " 
				+ "R2.REPLY_GROUP, REPLY_DEPTH, " 
				+ "USER_IDENTIFICATION, USER_NAME, USER_PASSWORD, " 
				+ "USER_PHONE, USER_NICKNAME, USER_EMAIL, USER_ADDRESS, USER_BIRTH, " 
				+ "USER_GENDER, USER_RECOMMENDER_ID " 
				+ "FROM (SELECT REPLY_GROUP, COUNT(REPLY_ID) - 1 REPLY_COUNT FROM TBL_REPLY GROUP BY REPLY_GROUP) R1 "
				+ "RIGHT OUTER JOIN VIEW_REPLY_USER R2 "
				+ "ON R1.REPLY_GROUP = R2.REPLY_GROUP AND R1.REPLY_GROUP = R2.REPLY_ID";
//				
//					R2 = replyDTO �� ���������� ��� ������ ��ģ ���̺��� ��� ����� ��� 		
//					�ٸ� �Ʒ��� ���ǽ��� ������ �ƴ϶� ��۸� �̾ƿ��� �ǹǷ�(�׷���̵�� ��۾��̵� ��������)right outer�� ����Ͽ� ���۵� �̾ƿ�  
		ReplyDTO replyDTO = null;//������� �־��� replyDTO�� ������ְ� null�� �־��� ������� ������ null�� �����ֱ� ����
		int index = 0;//���� ���� ���� �ֱ� �������ϱ� index++���ֱ� ���� ����
		ArrayList<ReplyDTO> replies = new ArrayList<ReplyDTO>();
		//�������� replyDTO�� ������� ArrayList ����.��ȸ�ϰ��� �ϴ� ����� ����� �𸣴� ArrayList�� �����. 
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);//��������
			resultSet = preparedStatement.executeQuery();//���� ������ ������� resultSet�� ����ش�
			
			while(resultSet.next()) {//���� �ϳ��� �̾ƿ�.ArrayList���Ͱ� ���� ������ while���
				index = 0;//�ε��� �ѹ� �������� �ʱ�ȭ.
				replyDTO = new ReplyDTO();
				replyDTO.setReplyCount(resultSet.getLong(++index));//replyDTO�� ���� ������ setter�� ���� �־���
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
				replies.add(replyDTO);//��� ������ ������ ArrayList�� �־��ش�
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
		return replies;//��� ArrayList ����
	}

//	���� ����
	public void deleteReReply(Long replyId) {//pk���޾ƿͼ� 
		String query = "DELETE FROM TBL_REPLY WHERE REPLY_ID = ?";//�����ϰ��� �ϴ� ���� ã�Ƽ� delete�� ����
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, replyId);//�Ű������� ���� pk�� ?�� �־��ش�
			preparedStatement.executeUpdate();//�����ϸ� executeUpdate�� ���� Ƚ���� int������ ������. �̰��� reply id�� �ϳ� �־ �����ϹǷ� 0�ƴϸ� 1
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
	
//	��� ����			
	public void deleteReply(Long groupId) {//����� �����Ǹ� ���۵� �����Ǿ�� ���� ���Ἲ�� �����ǹǷ� ��۱׷��� �Ű������� �Ἥ �ϴ� ������
		String query = "DELETE FROM TBL_REPLY WHERE REPLY_GROUP = ?";//���� ��� �׷��� ���� ��(���-����)�� ��� ã���� ����
	
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, groupId);
			System.out.println(preparedStatement.executeUpdate());//���� �̰��� replycount+1��ŭ ����� ����
			
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
	
//	��� ����
	public void update(ReplyVO replyVO) {//�����ϰ��� �ϴ� ��� ������ replyVO�� ��� �Ű������� ����
		String query = "UPDATE TBL_REPLY SET REPLY_CONTENT = ?, REPLY_UPDATE_DATE = SYSDATE WHERE REPLY_ID = ?";
//											�������			�����ϴϱ� �޼ҵ带 �����ϸ� update date�� �ý��� �ð����� ���� pk�� ��ã��
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, replyVO.getReplyContent());//ù��° ?�� �� ������ replyVO���� getter�̿� �־��� 
			preparedStatement.setLong(2, replyVO.getReplyId());//�ι�° ����ǥ�� �� ������ replyVO���� ������
			preparedStatement.executeUpdate();//����
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































