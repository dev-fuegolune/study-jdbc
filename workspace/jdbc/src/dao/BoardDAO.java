package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import domain.BoardDTO;
import domain.BoardVO;

public class BoardDAO {
	public Connection connection; //���� ��ü
	public PreparedStatement preparedStatement; //���� ���� ��ü
	public ResultSet resultSet; //��� ���̺� ��ü
	
//	�Խñ� �߰�
	public void insert(BoardVO boardVO) {//�߰��� �Խñ� ������ �Ű������� �Ѱ��ش�
		String query = "INSERT INTO TBL_BOARD"
				+ "(BOARD_ID, BOARD_TITLE, BOARD_CONTENT, BOARD_REGISTER_DATE, BOARD_UPDATE_DATE, USER_ID) "
				+ "VALUES(SEQ_BOARD.NEXTVAL, ?, ?, SYSDATE, SYSDATE, ?)";
//					pk���� �ߺ��� ������ ������ ����Ͽ� ����. ��ϳ��� ������Ʈ���� ���� �ð��̶� sysdate���	
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);//���� ����
			preparedStatement.setString(1, boardVO.getBoardTitle());//?�� ��� ���� ����
			preparedStatement.setString(2, boardVO.getBoardContent());
			preparedStatement.setLong(3, boardVO.getUserId());
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
//	�Խñ� ��ȸ
	public BoardDTO select(Long boardId) {//�Խñ� ��ȸ �� �Խñ� ������ �������� ��� �Ѱ��ֱ� ���ؼ� 
//		BoardDTO�� ���� ��������. 
		String query = "SELECT BOARD_ID, BOARD_TITLE, BOARD_CONTENT, BOARD_REGISTER_DATE, "
				+ " BOARD_UPDATE_DATE, U.USER_ID, USER_IDENTIFICATION, USER_NAME, USER_PASSWORD, "
				+ " USER_PHONE, USER_NICKNAME, USER_EMAIL, USER_ADDRESS, USER_BIRTH, USER_GENDER, "
				+ " USER_RECOMMENDER_ID "
				+ "FROM TBL_USER U JOIN TBL_BOARD B "
				+ "ON U.USER_ID = B.USER_ID AND BOARD_ID = ?";
//		������ �ִ� ���̺� �ΰ��� join�� ���� �����ص� �ΰ��� ���̺��� ���� ��θ� �����´�. 
//		�Ű������� �޾ƿ� �Խñ� ���̵� ���� ã���� �ϴ� �Խñ��� ã���� �Խñ� ���̺� �ִ� �������̵� ���� �Խñ� �ۼ��ڸ� ã���� ����� ����
		BoardDTO boardDTO = null;
//		������� ��� ������ DTO��ü ����
		int index = 0;
//		getLong �ʹ� �����ϱ� �ε����� �ڵ��ϼ�
		connection = DBConnecter.getConnection();
//		��� ����
		try {
			preparedStatement = connection.prepareStatement(query);//��������
			preparedStatement.setLong(1, boardId);//��������. 
			resultSet = preparedStatement.executeQuery();
			//���� ����. select�ϱ� ���� executeQuery�� �������ְ� ������� resultSet�� �־���
			if(resultSet.next()) {//�ϳ��� ������. ������ ���� �������� �Ŵϱ� if��
				boardDTO = new BoardDTO();//����� ���� ��ü ����
				boardDTO.setBoardId(resultSet.getLong(++index));//�ϳ��� ������� ����
				boardDTO.setBoardTitle(resultSet.getString(++index));
				boardDTO.setBoardContent(resultSet.getString(++index));
				boardDTO.setBoardRegisterDate(resultSet.getString(++index));
				boardDTO.setBoardUpdateDate(resultSet.getString(++index));
				boardDTO.setUserId(resultSet.getLong(++index));
				boardDTO.setUserIdentification(resultSet.getString(++index));
				boardDTO.setUserName(resultSet.getString(++index));
				boardDTO.setUserPassword(resultSet.getString(++index));
				boardDTO.setUserPhone(resultSet.getString(++index));
				boardDTO.setUserNickname(resultSet.getString(++index));
				boardDTO.setUserEmail(resultSet.getString(++index));
				boardDTO.setUserAddress(resultSet.getString(++index));
				boardDTO.setUserBirth(resultSet.getString(++index));
				boardDTO.setUserGender(resultSet.getString(++index));
				boardDTO.setUserRecommenderId(resultSet.getString(++index));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(resultSet != null) {
					resultSet.close();
				}
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
		
		return boardDTO;
	}
//	�Խñ� ����
	public void update(BoardVO boardVO) {
//		������ �Խñ� ������ �Ű������� ������
		String query = "UPDATE TBL_BOARD SET BOARD_TITLE = ?, BOARD_CONTENT = ?, BOARD_UPDATE_DATE = SYSDATE "
				+ "WHERE BOARD_ID = ?";
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, boardVO.getBoardTitle());//����
			preparedStatement.setString(2, boardVO.getBoardContent());//���� ����
			preparedStatement.setLong(3, boardVO.getBoardId());//�Խñ� pk�� ���ؼ� ������ �Խñ��� Ȯ����
			preparedStatement.executeUpdate();//���� ����
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
	
//	�Խñ� ����
	public void delete(Long boardId) {//�����ϱ� �Խñ� pk�� �Ű������� ������
		String query = "DELETE FROM TBL_BOARD WHERE BOARD_ID = ?";
//					�Ű������� ������ �Խñ��� Ȯ���� ����
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);//���� ����
			preparedStatement.setLong(1, boardId);//��������
			preparedStatement.executeUpdate();//��������
			
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
	
//	�Խñ� ��ü ��ȸ
	public ArrayList<BoardDTO> selectAll(){//��ü��ȸ�ϱ� ArrayList�� ����(�󸶳� ����������). DTO�� �Խñ� ������ �ۼ��� ���� ��� �Ѱ��ֱ� ���� ���� ����
		String query = "SELECT BOARD_ID, BOARD_TITLE, BOARD_CONTENT, BOARD_REGISTER_DATE, "
				+ " BOARD_UPDATE_DATE, U.USER_ID, USER_IDENTIFICATION, USER_NAME, USER_PASSWORD, "
				+ " USER_PHONE, USER_NICKNAME, USER_EMAIL, USER_ADDRESS, USER_BIRTH, USER_GENDER, "
				+ " USER_RECOMMENDER_ID "
				+ "FROM TBL_USER U JOIN TBL_BOARD B "
				+ "ON U.USER_ID = B.USER_ID";
//			�Խñ��� �������̵� fk�� �����Ƿ� user�� ��. ��ü��ȸ�ϱ� �Ű����� ���� join�� ���ؼ� �Խñ۰� �ۼ��� ���̺��� ��ģ�� ����
		
		BoardDTO boardDTO = null;//����� ���� DTO ����
		ArrayList<BoardDTO> boards = new ArrayList<BoardDTO>();//DTO�� ��� ArrayList ����
		int index = 0;
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);//��������
			resultSet = preparedStatement.executeQuery();//���� �����ؼ� resultSet�� �־��ְ�
			while(resultSet.next()) {//�������� ���� �𸣴ϱ� resultSet.next()���� false�� �ɶ����� ������ while�� ����
				index = 0;
				boardDTO = new BoardDTO();//����� ���� DTO��ü ����
				boardDTO.setBoardId(resultSet.getLong(++index));//������� �־���
				boardDTO.setBoardTitle(resultSet.getString(++index));
				boardDTO.setBoardContent(resultSet.getString(++index));
				boardDTO.setBoardRegisterDate(resultSet.getString(++index));
				boardDTO.setBoardUpdateDate(resultSet.getString(++index));
				boardDTO.setUserId(resultSet.getLong(++index));
				boardDTO.setUserIdentification(resultSet.getString(++index));
				boardDTO.setUserName(resultSet.getString(++index));
				boardDTO.setUserPassword(resultSet.getString(++index));
				boardDTO.setUserPhone(resultSet.getString(++index));
				boardDTO.setUserNickname(resultSet.getString(++index));
				boardDTO.setUserEmail(resultSet.getString(++index));
				boardDTO.setUserAddress(resultSet.getString(++index));
				boardDTO.setUserBirth(resultSet.getString(++index));
				boardDTO.setUserGender(resultSet.getString(++index));
				boardDTO.setUserRecommenderId(resultSet.getString(++index));
				boards.add(boardDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(resultSet != null) {
					resultSet.close();
				}
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
		
		return boards;
	}
}




















