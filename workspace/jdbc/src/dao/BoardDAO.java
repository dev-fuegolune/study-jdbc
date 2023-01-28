package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import domain.BoardDTO;
import domain.BoardVO;

public class BoardDAO {
	public Connection connection; //연결 객체
	public PreparedStatement preparedStatement; //쿼리 관리 객체
	public ResultSet resultSet; //결과 테이블 객체
	
//	게시글 추가
	public void insert(BoardVO boardVO) {//추가할 게시글 정보를 매개변수로 넘겨준다
		String query = "INSERT INTO TBL_BOARD"
				+ "(BOARD_ID, BOARD_TITLE, BOARD_CONTENT, BOARD_REGISTER_DATE, BOARD_UPDATE_DATE, USER_ID) "
				+ "VALUES(SEQ_BOARD.NEXTVAL, ?, ?, SYSDATE, SYSDATE, ?)";
//					pk값은 중복이 없도록 시퀀스 사용하여 세팅. 등록날과 업데이트날은 현재 시간이라서 sysdate사용	
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);//쿼리 전달
			preparedStatement.setString(1, boardVO.getBoardTitle());//?에 담길 변수 설정
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
//	게시글 조회
	public BoardDTO select(Long boardId) {//게시글 조회 시 게시글 정보와 유저정보 모두 넘겨주기 위해서 
//		BoardDTO를 만들어서 리턴해줌. 
		String query = "SELECT BOARD_ID, BOARD_TITLE, BOARD_CONTENT, BOARD_REGISTER_DATE, "
				+ " BOARD_UPDATE_DATE, U.USER_ID, USER_IDENTIFICATION, USER_NAME, USER_PASSWORD, "
				+ " USER_PHONE, USER_NICKNAME, USER_EMAIL, USER_ADDRESS, USER_BIRTH, USER_GENDER, "
				+ " USER_RECOMMENDER_ID "
				+ "FROM TBL_USER U JOIN TBL_BOARD B "
				+ "ON U.USER_ID = B.USER_ID AND BOARD_ID = ?";
//		디비버에 있는 테이블 두개를 join을 통해 합쳐준뒤 두개의 테이블의 정보 모두를 가져온다. 
//		매개변수로 받아온 게시글 아이디를 통해 찾고자 하는 게시글을 찾은후 게시글 테이블에 있는 유저아이디를 통해 게시글 작성자를 찾은후 결과값 리턴
		BoardDTO boardDTO = null;
//		결과값을 담아 돌려줄 DTO객체 선언
		int index = 0;
//		getLong 너무 많으니까 인덱스로 자동완성
		connection = DBConnecter.getConnection();
//		통로 설정
		try {
			preparedStatement = connection.prepareStatement(query);//쿼리전달
			preparedStatement.setLong(1, boardId);//변수세팅. 
			resultSet = preparedStatement.executeQuery();
			//쿼리 실행. select니까 쿼리 executeQuery로 실행해주고 결과값을 resultSet에 넣어줌
			if(resultSet.next()) {//하나씩 가져옴. 한정된 값을 가져오는 거니까 if문
				boardDTO = new BoardDTO();//결과값 담을 객체 생성
				boardDTO.setBoardId(resultSet.getLong(++index));//하나씩 순서대로 세팅
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
//	게시글 수정
	public void update(BoardVO boardVO) {
//		수정할 게시글 정보를 매개변수로 가져옴
		String query = "UPDATE TBL_BOARD SET BOARD_TITLE = ?, BOARD_CONTENT = ?, BOARD_UPDATE_DATE = SYSDATE "
				+ "WHERE BOARD_ID = ?";
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, boardVO.getBoardTitle());//제목
			preparedStatement.setString(2, boardVO.getBoardContent());//내용 수정
			preparedStatement.setLong(3, boardVO.getBoardId());//게시글 pk를 통해서 수정할 게시글을 확인후
			preparedStatement.executeUpdate();//쿼리 실행
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
	
//	게시글 삭제
	public void delete(Long boardId) {//삭제니까 게시글 pk만 매개변수로 가져옴
		String query = "DELETE FROM TBL_BOARD WHERE BOARD_ID = ?";
//					매개변수로 삭제할 게시글을 확인후 삭제
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);//쿼리 전달
			preparedStatement.setLong(1, boardId);//변수세팅
			preparedStatement.executeUpdate();//쿼리실행
			
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
	
//	게시글 전체 조회
	public ArrayList<BoardDTO> selectAll(){//전체조회니까 ArrayList로 리턴(얼마나 있을지미정). DTO는 게시글 정보와 작성자 정보 모두 넘겨주기 위해 만들어서 리턴
		String query = "SELECT BOARD_ID, BOARD_TITLE, BOARD_CONTENT, BOARD_REGISTER_DATE, "
				+ " BOARD_UPDATE_DATE, U.USER_ID, USER_IDENTIFICATION, USER_NAME, USER_PASSWORD, "
				+ " USER_PHONE, USER_NICKNAME, USER_EMAIL, USER_ADDRESS, USER_BIRTH, USER_GENDER, "
				+ " USER_RECOMMENDER_ID "
				+ "FROM TBL_USER U JOIN TBL_BOARD B "
				+ "ON U.USER_ID = B.USER_ID";
//			게시글이 유저아이디를 fk로 받으므로 user가 선. 전체조회니까 매개변수 없이 join을 통해서 게시글과 작성자 테이블을 합친뒤 리턴
		
		BoardDTO boardDTO = null;//결과값 담을 DTO 선언
		ArrayList<BoardDTO> boards = new ArrayList<BoardDTO>();//DTO가 담길 ArrayList 생성
		int index = 0;
		connection = DBConnecter.getConnection();
		try {
			preparedStatement = connection.prepareStatement(query);//쿼리전달
			resultSet = preparedStatement.executeQuery();//쿼리 실행해서 resultSet에 넣어주고
			while(resultSet.next()) {//언제까지 돌지 모르니까 resultSet.next()값이 false가 될때까지 돌도록 while문 실행
				index = 0;
				boardDTO = new BoardDTO();//결과값 담을 DTO객체 생성
				boardDTO.setBoardId(resultSet.getLong(++index));//결과값을 넣어줌
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




















