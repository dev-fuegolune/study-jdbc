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
	public Connection connection; //연결 객체
	public PreparedStatement preparedStatement; //쿼리 관리 객체
	public ResultSet resultSet; //결과 테이블 객체
	
//	아이디 중복검사
	public boolean checkId(String userIdentification) {
		String query = "SELECT COUNT(USER_ID) FROM TBL_USER WHERE USER_IDENTIFICATION = ?";
		//	유저의 ID(String)을 매개변수로 받아서 USER테이블에서 ID가 일치하는 회원이 있는지 세는 쿼리 
		boolean result = false;
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);//쿼리를 connection 객체를 통해 넘겨줌
			preparedStatement.setString(1, userIdentification);//첫번째 ?에 변수를 세팅해줌
			resultSet = preparedStatement.executeQuery();//쿼리 실행. select니까 executequery. 실행 결과를 결과테이블 객체에 넣어줌 
			
			resultSet.next();//next메소드를 통해서 하나씩 넘긴다. 더이상 없으면 false 있으면 true
			result = resultSet.getInt(1) == 0;//값을 가져오는 get()메소드를 통해서 select로 찾은 아이디가 동일한 유저 아이디 수가 0이면 중복된 아이디가 없는 것 
			
		} catch (SQLException e) {
			System.out.println("checkId(String) SQL문 오류");
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
		return result;//결과값 return
	}
	
//	회원가입
	public void insert(UserVO userVO) {//회원가입에 필요한 정보를 UserVO매개변수를 통해 받아옴
		 String queryForJoin = "INSERT INTO TBL_USER"
		            + "(USER_ID, USER_IDENTIFICATION, USER_NAME, USER_PASSWORD, USER_PHONE, USER_BIRTH, USER_EMAIL, USER_ADDRESS, USER_RECOMMENDER_ID) "
		            + "VALUES(SEQ_USER.NEXTVAL, ?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?, ?)";
//	user_Id는 pk라서 중복값이 있으면 안되므로 sequence를 사용 String변수를 dbeaver세팅된 형식에 맞춰서 date형식으로 바꿔서 전달 	
//		 기존 테이블에서 추천수 컬럼을 추가하여, 회원 정보 조회 시 추천 수도 같이 가져오도록 설계
//		 String queryForUpdateRecommendCount = 
//				 "UPDATE TBL_USER SET RECOMMEND_COUNT = RECOMMEND_COUNT + 1 WHERE USER_ID = ?";
//		 테이블 분리를 안했을 경우 user테이블에서 추천수도 수정
		 
//		 만약 추천 수 조회가 잦을 경우, 테이블을 따로 분리하여 서브키를 설정한 뒤 조회
//		 테이블이 분리되어서 회원가입 시 TBL_USER에 INSERT 후 TBL_RECOMMEND도 같이 INSERT가 필요하다.
//		 이 때 TRIGGER를 사용하여 TBL_USER에 INSERT이벤트 발생 시 TBL_RECOMMEND에도 자동으로 INSERT 되도록 설계
		 String queryForUpdateRecommendCount = 
				 "UPDATE TBL_RECOMMEND "
				 + "SET RECOMMEND_COUNT = RECOMMEND_COUNT + 1 "
				 + "WHERE USER_ID = ?";
//		 		회원가입시 추천인이 있으면 추천수를 1증가 시켜주는 쿼리
		 String queryForGetUserId = "SELECT USER_ID FROM TBL_USER WHERE USER_IDENTIFICATION = ?";
//										추천인 아이디를 찾아오는 쿼리	 
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
			
			if(userVO.getUserRecommenderId() != null) {//추천인 아이디가 있을경우에 user테이블에서 추천인 ID를 검색하여 가져온다
				preparedStatement = connection.prepareStatement(queryForGetUserId);
				preparedStatement.setString(1, userVO.getUserRecommenderId());
				resultSet = preparedStatement.executeQuery();
				
				if(resultSet.next()) { //추천수를 1증가 시키는 쿼리 실행.
					preparedStatement = connection.prepareStatement(queryForUpdateRecommendCount);
					preparedStatement.setLong(1, resultSet.getLong(1));
					preparedStatement.executeUpdate();
				}
			}

		} catch (SQLException e) {
			System.out.println("insert(UserVO) SQL문 오류");
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
//	로그인
//	로그인시 화면단에서 아이디비번 입력	->유저의 아이디와 비밀번호를 매개변수로 받음
	public boolean login(String userIdentification, String userPassword) {
		String query = "SELECT USER_ID FROM TBL_USER WHERE USER_IDENTIFICATION = ? AND USER_PASSWORD = ?";
//						매개변수로 받은 비번과 아이디와 일치하는 테이블이 있는지 select문으로 검색
		boolean check = false;
//		리턴하기 위해서 변수 설정. 
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, userIdentification);//?에 실제 변수를 넣기 위해서 setString 사용
			preparedStatement.setString(2, encrypt(userPassword));//테이블에 있는 비번은 모두 암호화된 상태이기 떄문에 받아온 비번을 encrypt메소드를 통해서 암호화
			
			resultSet = preparedStatement.executeQuery();//select문이니까 executeQuery로 실행 
			
			if(resultSet.next()) {//resultSet에 담긴 정보를 하나씩 넘겨줘서
				userId = resultSet.getLong(1);//유저의 id(pk)가져온 값을 userid에 담아줌. 이때 userid는 로그인 상태유지를 가정하기 위해 static 변수로 설정
				check = true;
			}
			
		} catch (SQLException e) {
			System.out.println("login(String, String) SQL문 오류");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {//마지막에 사용한 것부터 닫아줌
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
	
//	회원탈퇴
	public void delete() {//삭제는 보통 로그인상태에서 버튼 하나만 누르면 삭제->매개변수로 받아오지 않음.
		String query = "DELETE FROM TBL_USER WHERE USER_ID = ?"; //전역변수 userid로 테이블에서 삭제할 회원을 찾아서 삭제실행
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, userId);
			
			preparedStatement.executeUpdate();//delete니까 update로 실행
			
		} catch (SQLException e) {
			System.out.println("delete() SQL문 오류");
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
	
//	아이디 찾기
	public String findIdentification(String userPhone) {//폰 번호를 매개변수로 받음
		String query = "SELECT USER_IDENTIFICATION FROM TBL_USER WHERE USER_PHONE = ?";//받은 매개변수로 일치하는 유저id(String)을 찾는쿼리
		String userIdentification = null;
		
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);//connection객체를 통해서 쿼리를 db로 넘겨줌
			preparedStatement.setString(1, userPhone);//?에 매개변수 세팅

			resultSet = preparedStatement.executeQuery();//select문이니까 executeQurey
			
			if(resultSet.next()) {//일치하는 아이디가 있으면
				userIdentification = resultSet.getString(1);//get메소드를 통해서 리턴할 유저 아이디(String)을 가져옴
			}
			
		} catch (SQLException e) {
			System.out.println("findIdentification(String) SQL문 오류");
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
		
		return userIdentification;//아이디 리턴
	}
	
//	비밀번호 변경
	public void updateUserPassword(String userPassword) {//변경할 비번을 매개변수로 받아옴
		String query = "UPDATE TBL_USER SET USER_PASSWORD = ? WHERE USER_ID = ?";
		//로그인된 상태일테니 세션에서 userid를 가져와서 맞는 객체를 찾은후 매개변수로 받은 비번을 암호화한후 넣어줌
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, new String(Base64.getEncoder().encode(userPassword.getBytes())));
			preparedStatement.setLong(2, userId);
			
			preparedStatement.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("updateUserPassword(String) SQL문 오류");
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
	
//	회원정보 수정
	public void update(UserVO userVO) {//수정해야 하는 정보를 받아오기 위해 userVO를 받아온다
		String query = "UPDATE TBL_USER SET USER_NICKNAME = ?, USER_GENDER = ?, USER_BIRTH = TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS') WHERE USER_ID = ?";
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, userVO.getUserNickname());
			preparedStatement.setString(2, userVO.getUserGender());
			preparedStatement.setString(3, userVO.getUserBirth());//수정하고자 하는 정보를 매개변수로 받아온 값으로 변경. 
			preparedStatement.setLong(4, userId);//로그인한 상태
			
			preparedStatement.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("update(UserVO) SQL문 오류");
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
	
//	회원정보 조회
	public UserVO select(/* Long userId */) {//일반회원이 사용하는 조회 메소드
		String query = 
				"SELECT USER_ID, USER_IDENTIFICATION, USER_NAME, USER_PASSWORD,"
				+ " USER_PHONE, USER_NICKNAME, USER_EMAIL, USER_ADDRESS, USER_BIRTH, "
				+ "USER_GENDER, USER_RECOMMENDER_ID "
				+ "FROM TBL_USER WHERE USER_ID = ?";
		UserVO userVO = null;
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, userId);//로그인한 상태이면 조회 정보를 넘겨줌
			resultSet = preparedStatement.executeQuery();//결과 객체를 resultset에 담아준다
			
			if(resultSet.next()) {//next로 하나씩 넘겨줌. 몇개 넘겨줄지 알기때문에 if문사용
				userVO = new UserVO();
				userVO.setUserId(resultSet.getLong(1));//userVO에 세팅
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
			System.out.println("select(Long) SQL문 오류");
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
		
		return userVO;//결과 객체 리턴
	}
	
	public UserVO select(Long userId) {//관리자가 사용하는 select문. 위의 메소드와 동일하나 관리자가 사용하는것이므로 pk값인 userid(long)를 가져온다
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
			System.out.println("select(Long) SQL문 오류");
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
	
	public UserVO select(String userIdentification) {//추천수에서 사용할 수 있도록 매개변수가 userid(String)인 메소드 오버로딩
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
			System.out.println("select(Long) SQL문 오류");
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
	
//	추천수
	public Long getCountOfRecommenderId(String userIdentification) {//유저의 아이디를 가져와서 추천수를 찾음
//		String query = "SELECT RECOMMEND_COUNT FROM TBL_USER WHERE USER_ID = ?";
//						테이블 분리 안했을 떄 유저 테이블에서 추천수 찾음 ?=userid(static)
		String query = "SELECT RECOMMEND_COUNT FROM TBL_RECOMMEND WHERE USER_ID = ?";
//								추천수 검색이 많아서 분리했을경우 분리한 추천수 테이블에서 추천수 가져옴 ?
		Long recommendCount = 0L;
		Long userId = select(userIdentification).getUserId();//회원 정보 조회 메소드를 통해서 매개변수를 넘겨준뒤 userVO를 받아서 거기서 userid(pk)를 가져옴
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, userId);//가져온 값을 ?에 세팅
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
				recommendCount = resultSet.getLong(1);//결과값(추천수)을 recommandCount에 담아줌
			}
		} catch (SQLException e) {
			System.out.println("getCountOfRecommenderId(Long) SQL문 오류");
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
		return recommendCount;//결과값 리턴
	}
	
//	나를 추천한 사람
	public ArrayList<UserVO> selectRecommenders(){//나를 추천한 사람이 몇이나 있을지 모르니ArrayList에 userVO객체를 담음
		String query = "SELECT USER_ID, USER_IDENTIFICATION, USER_NAME, USER_PASSWORD,"
				+ " USER_PHONE, USER_NICKNAME, USER_EMAIL, USER_ADDRESS, USER_BIRTH, "
				+ "USER_GENDER, USER_RECOMMENDER_ID FROM TBL_USER WHERE USER_RECOMMENDER_ID = ?";//나의 아이디가 추천인아이디인 사람의 객체를 찾음
		String userIdentification = select(userId).getUserIdentification();
		//로그인한 상태에서 나를 추천한 사람 버튼을 누르므로 세션에 저장된 userid를 가져와서 회원정보조회 메소드를 사용하여 나의id(String)을 가져옴
		UserVO userVO = null;
		//추천한 사람 정보를 담을 객체 선언
		ArrayList<UserVO> users = new ArrayList<UserVO>();
		//추천한 사람들의 정보를 리턴할 ArrayList객체 만듦		
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);//쿼리전달
			preparedStatement.setString(1, userIdentification);//조회변수로 가져온 유저아이디를 ?에 세팅해줌
			
			resultSet = preparedStatement.executeQuery();//쿼리 실행해서 찾은 결과를 결과객체에 담아줌
			
			while(resultSet.next()) {//하나씩 넘겨줌
				userVO = new UserVO();//넘겨준 값을 세팅해줄 userVO객체를 만듦
				userVO.setUserId(resultSet.getLong(1));//넘겨준 값을userVO에 세팅
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
				users.add(userVO);//arrayList에넣어줌
			}
			
		} catch (SQLException e) {
			System.out.println("selectRecommenders() SQL문 오류");
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
		return users;//ArrayList리턴
	}
	
//	내가 추천한 사람
	public UserVO getMyRecommender() {
		String query = "SELECT USER_ID, USER_IDENTIFICATION, USER_NAME, USER_PASSWORD,"
				+ " USER_PHONE, USER_NICKNAME, USER_EMAIL, USER_ADDRESS, USER_BIRTH, "
				+ "USER_GENDER, USER_RECOMMENDER_ID FROM TBL_USER WHERE USER_IDENTIFICATION = "//서브쿼리를 실행하여 나의 추천인 아이디를 통해 추천인정보가 담긴 객체를 찾는다
				+ "("
				+ "SELECT USER_RECOMMENDER_ID FROM TBL_USER WHERE USER_ID = ?"//세팅한 유저 아이디로 내가 추천한 사람의 아이디를 가져와서 
				+ ")";
		UserVO userVO = null;
		//서브쿼리 실행 이유? 사용자는 로그인 상태에서 나를 추천한 사람 버튼만 누르면 결과값이 나와야 하기 때문에 나의 userid(세션에 담긴 아이디(long))를 통해 내가 추천한 사람 객체를 찾아야하기 때문에 서브쿼리를 통해서먼저
//		내가 추천한 사람 아이디를 찾아와서 where절에서 useridenfication과 비교한다. 
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, userId);//로그인한 상태이니 세션에 있는 로그인 정보를 가져와서 세팅
			
			resultSet = preparedStatement.executeQuery();//쿼리 실행해서 결과값을 resultSet에 담아줌
			
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
			System.out.println("getMyRecommender() SQL문 오류");
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
	
	public String encrypt(String password) {//Base64방식으로 암호화된 비번은 바이트 방식으로 리턴됨으로 String으로 변환 
		return new String(Base64.getEncoder().encode(password.getBytes()));
	}
	
}





















