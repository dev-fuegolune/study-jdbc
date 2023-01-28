package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;

import domain.UserVO;

public class UserDAO {

	public static Long userId;
	public Connection connection; //���� ��ü
	public PreparedStatement preparedStatement; //���� ���� ��ü
	public ResultSet resultSet; //��� ���̺� ��ü
	
//	���̵� �ߺ��˻�
	public boolean checkId(String userIdentification) {
		String query = "SELECT COUNT(USER_ID) FROM TBL_USER WHERE USER_IDENTIFICATION = ?";
		//	������ ID(String)�� �Ű������� �޾Ƽ� USER���̺��� ID�� ��ġ�ϴ� ȸ���� �ִ��� ���� ���� 
		boolean result = false;
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);//������ connection ��ü�� ���� �Ѱ���
			preparedStatement.setString(1, userIdentification);//ù��° ?�� ������ ��������
			resultSet = preparedStatement.executeQuery();//���� ����. select�ϱ� executequery. ���� ����� ������̺� ��ü�� �־��� 
			
			resultSet.next();//next�޼ҵ带 ���ؼ� �ϳ��� �ѱ��. ���̻� ������ false ������ true
			result = resultSet.getInt(1) == 0;//���� �������� get()�޼ҵ带 ���ؼ� select�� ã�� ���̵� ������ ���� ���̵� ���� 0�̸� �ߺ��� ���̵� ���� �� 
			
		} catch (SQLException e) {
			System.out.println("checkId(String) SQL�� ����");
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
		return result;//����� return
	}
	
//	ȸ������
	public void insert(UserVO userVO) {//ȸ�����Կ� �ʿ��� ������ UserVO�Ű������� ���� �޾ƿ�
		 String queryForJoin = "INSERT INTO TBL_USER"
		            + "(USER_ID, USER_IDENTIFICATION, USER_NAME, USER_PASSWORD, USER_PHONE, USER_BIRTH, USER_EMAIL, USER_ADDRESS, USER_RECOMMENDER_ID) "
		            + "VALUES(SEQ_USER.NEXTVAL, ?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?, ?)";
//	user_Id�� pk�� �ߺ����� ������ �ȵǹǷ� sequence�� ��� String������ dbeaver���õ� ���Ŀ� ���缭 date�������� �ٲ㼭 ���� 	
//		 ���� ���̺��� ��õ�� �÷��� �߰��Ͽ�, ȸ�� ���� ��ȸ �� ��õ ���� ���� ���������� ����
//		 String queryForUpdateRecommendCount = 
//				 "UPDATE TBL_USER SET RECOMMEND_COUNT = RECOMMEND_COUNT + 1 WHERE USER_ID = ?";
//		 ���̺� �и��� ������ ��� user���̺��� ��õ���� ����
		 
//		 ���� ��õ �� ��ȸ�� ���� ���, ���̺��� ���� �и��Ͽ� ����Ű�� ������ �� ��ȸ
//		 ���̺��� �и��Ǿ ȸ������ �� TBL_USER�� INSERT �� TBL_RECOMMEND�� ���� INSERT�� �ʿ��ϴ�.
//		 �� �� TRIGGER�� ����Ͽ� TBL_USER�� INSERT�̺�Ʈ �߻� �� TBL_RECOMMEND���� �ڵ����� INSERT �ǵ��� ����
		 String queryForUpdateRecommendCount = 
				 "UPDATE TBL_RECOMMEND "
				 + "SET RECOMMEND_COUNT = RECOMMEND_COUNT + 1 "
				 + "WHERE USER_ID = ?";
//		 		ȸ�����Խ� ��õ���� ������ ��õ���� 1���� �����ִ� ����
		 String queryForGetUserId = "SELECT USER_ID FROM TBL_USER WHERE USER_IDENTIFICATION = ?";
//										��õ�� ���̵� ã�ƿ��� ����	 
		 connection = DBConnecter.getConnection();
		 try {
			preparedStatement = connection.prepareStatement(queryForJoin);
			preparedStatement = connection.prepareStatement(queryForJoin);
			preparedStatement.setString(1, userVO.getUserIdentification());
			preparedStatement.setString(2, userVO.getUserName());
			preparedStatement.setString(3, encrypt(userVO.getUserPassword()));
			preparedStatement.setString(4, userVO.getUserPhone());
			preparedStatement.setString(5, userVO.getUserBirth());
			preparedStatement.setString(6, userVO.getUserEmail());
			preparedStatement.setString(7, userVO.getUserAddress());
			preparedStatement.setString(8, userVO.getUserRecommenderId());
			preparedStatement.executeUpdate();
			
			if(userVO.getUserRecommenderId() != null) {//��õ�� ���̵� ������쿡 user���̺��� ��õ�� ID�� �˻��Ͽ� �����´�
				preparedStatement = connection.prepareStatement(queryForGetUserId);
				preparedStatement.setString(1, userVO.getUserRecommenderId());
				resultSet = preparedStatement.executeQuery();
				
				if(resultSet.next()) { //��õ���� 1���� ��Ű�� ���� ����.
					preparedStatement = connection.prepareStatement(queryForUpdateRecommendCount);
					preparedStatement.setLong(1, resultSet.getLong(1));
					preparedStatement.executeUpdate();
				}
			}

		} catch (SQLException e) {
			System.out.println("insert(UserVO) SQL�� ����");
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
	}
//	�α���
//	�α��ν� ȭ��ܿ��� ���̵��� �Է�	->������ ���̵�� ��й�ȣ�� �Ű������� ����
	public boolean login(String userIdentification, String userPassword) {
		String query = "SELECT USER_ID FROM TBL_USER WHERE USER_IDENTIFICATION = ? AND USER_PASSWORD = ?";
//						�Ű������� ���� ����� ���̵�� ��ġ�ϴ� ���̺��� �ִ��� select������ �˻�
		boolean check = false;
//		�����ϱ� ���ؼ� ���� ����. 
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, userIdentification);//?�� ���� ������ �ֱ� ���ؼ� setString ���
			preparedStatement.setString(2, encrypt(userPassword));//���̺� �ִ� ����� ��� ��ȣȭ�� �����̱� ������ �޾ƿ� ����� encrypt�޼ҵ带 ���ؼ� ��ȣȭ
			
			resultSet = preparedStatement.executeQuery();//select���̴ϱ� executeQuery�� ���� 
			
			if(resultSet.next()) {//resultSet�� ��� ������ �ϳ��� �Ѱ��༭
				userId = resultSet.getLong(1);//������ id(pk)������ ���� userid�� �����. �̶� userid�� �α��� ���������� �����ϱ� ���� static ������ ����
				check = true;
			}
			
		} catch (SQLException e) {
			System.out.println("login(String, String) SQL�� ����");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {//�������� ����� �ͺ��� �ݾ���
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
		return check;
	}
	
//	ȸ��Ż��
	public void delete() {//������ ���� �α��λ��¿��� ��ư �ϳ��� ������ ����->�Ű������� �޾ƿ��� ����.
		String query = "DELETE FROM TBL_USER WHERE USER_ID = ?"; //�������� userid�� ���̺��� ������ ȸ���� ã�Ƽ� ��������
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, userId);
			
			preparedStatement.executeUpdate();//delete�ϱ� update�� ����
			
		} catch (SQLException e) {
			System.out.println("delete() SQL�� ����");
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
	
//	���̵� ã��
	public String findIdentification(String userPhone) {//�� ��ȣ�� �Ű������� ����
		String query = "SELECT USER_IDENTIFICATION FROM TBL_USER WHERE USER_PHONE = ?";//���� �Ű������� ��ġ�ϴ� ����id(String)�� ã������
		String userIdentification = null;
		
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);//connection��ü�� ���ؼ� ������ db�� �Ѱ���
			preparedStatement.setString(1, userPhone);//?�� �Ű����� ����

			resultSet = preparedStatement.executeQuery();//select���̴ϱ� executeQurey
			
			if(resultSet.next()) {//��ġ�ϴ� ���̵� ������
				userIdentification = resultSet.getString(1);//get�޼ҵ带 ���ؼ� ������ ���� ���̵�(String)�� ������
			}
			
		} catch (SQLException e) {
			System.out.println("findIdentification(String) SQL�� ����");
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
		
		return userIdentification;//���̵� ����
	}
	
//	��й�ȣ ����
	public void updateUserPassword(String userPassword) {//������ ����� �Ű������� �޾ƿ�
		String query = "UPDATE TBL_USER SET USER_PASSWORD = ? WHERE USER_ID = ?";
		//�α��ε� �������״� ���ǿ��� userid�� �����ͼ� �´� ��ü�� ã���� �Ű������� ���� ����� ��ȣȭ���� �־���
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, new String(Base64.getEncoder().encode(userPassword.getBytes())));
			preparedStatement.setLong(2, userId);
			
			preparedStatement.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("updateUserPassword(String) SQL�� ����");
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
	
//	ȸ������ ����
	public void update(UserVO userVO) {//�����ؾ� �ϴ� ������ �޾ƿ��� ���� userVO�� �޾ƿ´�
		String query = "UPDATE TBL_USER SET USER_NICKNAME = ?, USER_GENDER = ?, USER_BIRTH = TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS') WHERE USER_ID = ?";
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, userVO.getUserNickname());
			preparedStatement.setString(2, userVO.getUserGender());
			preparedStatement.setString(3, userVO.getUserBirth());//�����ϰ��� �ϴ� ������ �Ű������� �޾ƿ� ������ ����. 
			preparedStatement.setLong(4, userId);//�α����� ����
			
			preparedStatement.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("update(UserVO) SQL�� ����");
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
	
//	ȸ������ ��ȸ
	public UserVO select(/* Long userId */) {//�Ϲ�ȸ���� ����ϴ� ��ȸ �޼ҵ�
		String query = 
				"SELECT USER_ID, USER_IDENTIFICATION, USER_NAME, USER_PASSWORD,"
				+ " USER_PHONE, USER_NICKNAME, USER_EMAIL, USER_ADDRESS, USER_BIRTH, "
				+ "USER_GENDER, USER_RECOMMENDER_ID "
				+ "FROM TBL_USER WHERE USER_ID = ?";
		UserVO userVO = null;
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, userId);//�α����� �����̸� ��ȸ ������ �Ѱ���
			resultSet = preparedStatement.executeQuery();//��� ��ü�� resultset�� ����ش�
			
			if(resultSet.next()) {//next�� �ϳ��� �Ѱ���. � �Ѱ����� �˱⶧���� if�����
				userVO = new UserVO();
				userVO.setUserId(resultSet.getLong(1));//userVO�� ����
				userVO.setUserIdentification(resultSet.getString(2));
				userVO.setUserName(resultSet.getString(3));
				userVO.setUserPassword(resultSet.getString(4));
				userVO.setUserPhone(resultSet.getString(5));
				userVO.setUserNickname(resultSet.getString(6));
				userVO.setUserEmail(resultSet.getString(7));
				userVO.setUserAddress(resultSet.getString(8));
				userVO.setUserBirth(resultSet.getString(9));
				userVO.setUserGender(resultSet.getString(10));
				userVO.setUserRecommenderId(resultSet.getString(11));
			}
			
		} catch (SQLException e) {
			System.out.println("select(Long) SQL�� ����");
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
		
		return userVO;//��� ��ü ����
	}
	
	public UserVO select(Long userId) {//�����ڰ� ����ϴ� select��. ���� �޼ҵ�� �����ϳ� �����ڰ� ����ϴ°��̹Ƿ� pk���� userid(long)�� �����´�
		String query = 
				"SELECT USER_ID, USER_IDENTIFICATION, USER_NAME, USER_PASSWORD,"
						+ " USER_PHONE, USER_NICKNAME, USER_EMAIL, USER_ADDRESS, USER_BIRTH, "
						+ "USER_GENDER, USER_RECOMMENDER_ID "
						+ "FROM TBL_USER WHERE USER_ID = ?";
		UserVO userVO = null;
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, userId);
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
				userVO = new UserVO();
				userVO.setUserId(resultSet.getLong(1));
				userVO.setUserIdentification(resultSet.getString(2));
				userVO.setUserName(resultSet.getString(3));
				userVO.setUserPassword(resultSet.getString(4));
				userVO.setUserPhone(resultSet.getString(5));
				userVO.setUserNickname(resultSet.getString(6));
				userVO.setUserEmail(resultSet.getString(7));
				userVO.setUserAddress(resultSet.getString(8));
				userVO.setUserBirth(resultSet.getString(9));
				userVO.setUserGender(resultSet.getString(10));
				userVO.setUserRecommenderId(resultSet.getString(11));
			}
			
		} catch (SQLException e) {
			System.out.println("select(Long) SQL�� ����");
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
		
		return userVO;
	}
	
	public UserVO select(String userIdentification) {//��õ������ ����� �� �ֵ��� �Ű������� userid(String)�� �޼ҵ� �����ε�
		String query = 
				"SELECT USER_ID, USER_IDENTIFICATION, USER_NAME, USER_PASSWORD,"
						+ " USER_PHONE, USER_NICKNAME, USER_EMAIL, USER_ADDRESS, USER_BIRTH, "
						+ "USER_GENDER, USER_RECOMMENDER_ID "
						+ "FROM TBL_USER WHERE USER_IDENTIFICATION = ?";
		UserVO userVO = null;
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, userIdentification);
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
				userVO = new UserVO();
				userVO.setUserId(resultSet.getLong(1));
				userVO.setUserIdentification(resultSet.getString(2));
				userVO.setUserName(resultSet.getString(3));
				userVO.setUserPassword(resultSet.getString(4));
				userVO.setUserPhone(resultSet.getString(5));
				userVO.setUserNickname(resultSet.getString(6));
				userVO.setUserEmail(resultSet.getString(7));
				userVO.setUserAddress(resultSet.getString(8));
				userVO.setUserBirth(resultSet.getString(9));
				userVO.setUserGender(resultSet.getString(10));
				userVO.setUserRecommenderId(resultSet.getString(11));
			}
			
		} catch (SQLException e) {
			System.out.println("select(Long) SQL�� ����");
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
		
		return userVO;
	}
	
//	��õ��
	public Long getCountOfRecommenderId(String userIdentification) {//������ ���̵� �����ͼ� ��õ���� ã��
//		String query = "SELECT RECOMMEND_COUNT FROM TBL_USER WHERE USER_ID = ?";
//						���̺� �и� ������ �� ���� ���̺��� ��õ�� ã�� ?=userid(static)
		String query = "SELECT RECOMMEND_COUNT FROM TBL_RECOMMEND WHERE USER_ID = ?";
//								��õ�� �˻��� ���Ƽ� �и�������� �и��� ��õ�� ���̺��� ��õ�� ������ ?
		Long recommendCount = 0L;
		Long userId = select(userIdentification).getUserId();//ȸ�� ���� ��ȸ �޼ҵ带 ���ؼ� �Ű������� �Ѱ��ص� userVO�� �޾Ƽ� �ű⼭ userid(pk)�� ������
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, userId);//������ ���� ?�� ����
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
				recommendCount = resultSet.getLong(1);//�����(��õ��)�� recommandCount�� �����
			}
		} catch (SQLException e) {
			System.out.println("getCountOfRecommenderId(Long) SQL�� ����");
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
		return recommendCount;//����� ����
	}
	
//	���� ��õ�� ���
	public ArrayList<UserVO> selectRecommenders(){//���� ��õ�� ����� ���̳� ������ �𸣴�ArrayList�� userVO��ü�� ����
		String query = "SELECT USER_ID, USER_IDENTIFICATION, USER_NAME, USER_PASSWORD,"
				+ " USER_PHONE, USER_NICKNAME, USER_EMAIL, USER_ADDRESS, USER_BIRTH, "
				+ "USER_GENDER, USER_RECOMMENDER_ID FROM TBL_USER WHERE USER_RECOMMENDER_ID = ?";//���� ���̵� ��õ�ξ��̵��� ����� ��ü�� ã��
		String userIdentification = select(userId).getUserIdentification();
		//�α����� ���¿��� ���� ��õ�� ��� ��ư�� �����Ƿ� ���ǿ� ����� userid�� �����ͼ� ȸ��������ȸ �޼ҵ带 ����Ͽ� ����id(String)�� ������
		UserVO userVO = null;
		//��õ�� ��� ������ ���� ��ü ����
		ArrayList<UserVO> users = new ArrayList<UserVO>();
		//��õ�� ������� ������ ������ ArrayList��ü ����		
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);//��������
			preparedStatement.setString(1, userIdentification);//��ȸ������ ������ �������̵� ?�� ��������
			
			resultSet = preparedStatement.executeQuery();//���� �����ؼ� ã�� ����� �����ü�� �����
			
			while(resultSet.next()) {//�ϳ��� �Ѱ���
				userVO = new UserVO();//�Ѱ��� ���� �������� userVO��ü�� ����
				userVO.setUserId(resultSet.getLong(1));//�Ѱ��� ����userVO�� ����
				userVO.setUserIdentification(resultSet.getString(2));
				userVO.setUserName(resultSet.getString(3));
				userVO.setUserPassword(resultSet.getString(4));
				userVO.setUserPhone(resultSet.getString(5));
				userVO.setUserNickname(resultSet.getString(6));
				userVO.setUserEmail(resultSet.getString(7));
				userVO.setUserAddress(resultSet.getString(8));
				userVO.setUserBirth(resultSet.getString(9));
				userVO.setUserGender(resultSet.getString(10));
				userVO.setUserRecommenderId(resultSet.getString(11));
				users.add(userVO);//arrayList���־���
			}
			
		} catch (SQLException e) {
			System.out.println("selectRecommenders() SQL�� ����");
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
		return users;//ArrayList����
	}
	
//	���� ��õ�� ���
	public UserVO getMyRecommender() {
		String query = "SELECT USER_ID, USER_IDENTIFICATION, USER_NAME, USER_PASSWORD,"
				+ " USER_PHONE, USER_NICKNAME, USER_EMAIL, USER_ADDRESS, USER_BIRTH, "
				+ "USER_GENDER, USER_RECOMMENDER_ID FROM TBL_USER WHERE USER_IDENTIFICATION = "//���������� �����Ͽ� ���� ��õ�� ���̵� ���� ��õ�������� ��� ��ü�� ã�´�
				+ "("
				+ "SELECT USER_RECOMMENDER_ID FROM TBL_USER WHERE USER_ID = ?"//������ ���� ���̵�� ���� ��õ�� ����� ���̵� �����ͼ� 
				+ ")";
		UserVO userVO = null;
		//�������� ���� ����? ����ڴ� �α��� ���¿��� ���� ��õ�� ��� ��ư�� ������ ������� ���;� �ϱ� ������ ���� userid(���ǿ� ��� ���̵�(long))�� ���� ���� ��õ�� ��� ��ü�� ã�ƾ��ϱ� ������ ���������� ���ؼ�����
//		���� ��õ�� ��� ���̵� ã�ƿͼ� where������ useridenfication�� ���Ѵ�. 
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, userId);//�α����� �����̴� ���ǿ� �ִ� �α��� ������ �����ͼ� ����
			
			resultSet = preparedStatement.executeQuery();//���� �����ؼ� ������� resultSet�� �����
			
			if(resultSet.next()) {
				userVO = new UserVO();
				userVO.setUserId(resultSet.getLong(1));
				userVO.setUserIdentification(resultSet.getString(2));
				userVO.setUserName(resultSet.getString(3));
				userVO.setUserPassword(resultSet.getString(4));
				userVO.setUserPhone(resultSet.getString(5));
				userVO.setUserNickname(resultSet.getString(6));
				userVO.setUserEmail(resultSet.getString(7));
				userVO.setUserAddress(resultSet.getString(8));
				userVO.setUserBirth(resultSet.getString(9));
				userVO.setUserGender(resultSet.getString(10));
				userVO.setUserRecommenderId(resultSet.getString(11));
			}
			
		} catch (SQLException e) {
			System.out.println("getMyRecommender() SQL�� ����");
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
		return userVO;
	}
	
	public String encrypt(String password) {//Base64������� ��ȣȭ�� ����� ����Ʈ ������� ���ϵ����� String���� ��ȯ 
		return new String(Base64.getEncoder().encode(password.getBytes()));
	}
	
}





















