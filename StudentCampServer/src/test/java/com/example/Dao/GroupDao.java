//分组
package com.example.Dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.HashMap;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.model.GroupMsg;
import com.example.model.CampusStudent;
import com.example.model.GroupStudent;
import com.example.model.PunchClockManager;
import com.example.model.PunchClockStudent;
import com.example.modelInterface.PunchClockMapper;
import com.example.modelInterface.StuGroupMapper;

@Service
@MapperScan("com.example.modelInterface.stuGroupMapper")
public class GroupDao extends SuperDao{
	@Autowired
	private StuGroupMapper stuGroupMapper;
	public GroupDao() {
		super();
	}
	
	//查询分组设置信息
	public Map<String, Object> queryGroupMsg() throws DataAccessException {
		this.responseBody.remove("list");
		List<GroupMsg> groupMsg = stuGroupMapper.queryMsg();
		if(!groupMsg.isEmpty()) {
			this.SetSuccess();
			this.responseBody.put("list", groupMsg.get(0));
		}
		else {
			this.SetError();
			this.responseBody.put("errorMessage", "无信息");
			
		}
		return this.responseBody;
	}
	//设置分组信息
	public Map<String, Object> setGroupMsg(String groupNum, String groupStuNum,String stuSex,String stuGrade,String stuSchool,String stuHobby) throws DataAccessException {
		this.responseBody.remove("list");
		List<GroupMsg> groupMsg = stuGroupMapper.queryMsg();
		if(!groupMsg.isEmpty()) {
			return UpDateGroupMsg(groupNum,groupStuNum,stuSex,stuGrade,stuSchool,stuHobby);
		}
		else {
			return InsertGroupMsg(groupNum,groupStuNum,stuSex,stuGrade,stuSchool,stuHobby);
		}
	}
	
	//更新签到信息
	public Map<String, Object> UpDateGroupMsg(String groupNum, String groupStuNum,String stuSex,String stuGrade,String stuSchool,String stuHobby) throws DataAccessException {
		this.responseBody.remove("list");
		int res = stuGroupMapper.updateMsg(groupNum, groupStuNum, stuSex, stuGrade, stuSchool, stuHobby);
		if(res != 0) {
			this.SetSuccess();
		}
		else {
			this.SetError();
			this.responseBody.put("errorMessage", "更新分组信息失败");
			
		}
		return this.responseBody;
	}
	//插入报名信息
	public Map<String, Object> InsertGroupMsg(String groupNum, String groupStuNum,String stuSex,String stuGrade,String stuSchool,String stuHobby) throws DataAccessException {
		this.responseBody.remove("list");
		int res = stuGroupMapper.insert(groupNum, groupStuNum, stuSex, stuGrade, stuSchool, stuHobby);
		if(res != 0) {
			this.SetSuccess();
		}
		else {
			this.SetError();
			this.responseBody.put("errorMessage", "插入分组信息失败");
			
		}
		return this.responseBody;
	}
	//确认分组
	public Map<String, Object> confirmGroup() throws DataAccessException {
		this.responseBody.remove("list");
		int res = stuGroupMapper.updateIsFinish();
		if(res != 0) {
			this.SetSuccess();
		}
		else {
			this.SetError();
			this.responseBody.put("errorMessage", "确认失败");
			
		}
		return this.responseBody;
	}
	//调整分组
	public Map<String, Object> adjustGroup(String userName,String groupId) throws DataAccessException {
		this.responseBody.remove("list");
		int res = stuGroupMapper.updateStu(userName, groupId);
		if(res != 0) {
			this.SetSuccess();
		}
		else {
			this.SetError();
			this.responseBody.put("errorMessage", "确认失败");
			
		}
		return this.responseBody;
	}
	//拿到分组信息-管理者
	public Map<String, Object> queryGroupManager() throws DataAccessException {
		this.responseBody.remove("list");
		List<GroupStudent> students = stuGroupMapper.queryAllGroupStudent();
		if(!students.isEmpty()) {
			this.SetSuccess();
			this.responseBody.put("list", students);
		}
		else {
			this.SetError();
			this.responseBody.put("errorMessage", "无信息");		
		}
		return this.responseBody;
	}
	//拿到分组信息-教师
	public Map<String, Object> queryGroupTeacher(String teacherId) throws DataAccessException {
		this.responseBody.remove("list");
		Map<String, Object> result = new HashMap<String,Object>();
		List<String> schools = stuGroupMapper.querySchoolByTeacher(teacherId);
		List<GroupStudent> students = stuGroupMapper.queryGroupStudentBySchoolName(schools.get(0));
		if(!students.isEmpty()) {
			this.SetSuccess();
			this.responseBody.put("list", students);
		}
		else {
			this.SetError();
			this.responseBody.put("errorMessage", "无信息");		
		}
		return this.responseBody;
	}
	//拿到分组信息-学生
	public Map<String, Object> 	queryGroupStudent(String userName) throws DataAccessException {
		this.responseBody.remove("list");
		List<String> groupId = stuGroupMapper.queryGroupIdByUserName(userName);
		if(!groupId.isEmpty()) {
			List<GroupStudent> students= stuGroupMapper.queryGroupMsgByGroupId(groupId.get(0));
			this.SetSuccess();
			this.responseBody.put("list", students);
		}
		else {
			this.SetError();
			this.responseBody.put("errorMessage", "无信息");		
		}
		return this.responseBody;
	}
	
	
	public Map<String, Object> 	queryTotalNum() throws DataAccessException {
		this.responseBody.remove("list");
		List<CampusStudent> students = stuGroupMapper.queryAllStudent();
		if(!students.isEmpty()) {
			this.SetSuccess();
			this.responseBody.put("list", students.size());
		}
		else {
			this.SetError();
			this.responseBody.put("errorMessage", "无信息");		
		}
		return this.responseBody;
	}
	public Map<String, Object> 	resetDivide() throws DataAccessException {
		this.responseBody.remove("list");
		int res = stuGroupMapper.deleteAll();
		if(res != 0) {
			this.SetSuccess();
			return divide();
		}
		else {
			this.SetError();
			this.responseBody.put("errorMessage", "无信息");		
		}
		return this.responseBody;
	}
	//学生分组
	public Map<String, Object> 	divide() throws DataAccessException {
		this.responseBody.remove("list");
		List<GroupMsg> groupMsg = stuGroupMapper.queryMsg();
		if(!groupMsg.isEmpty()) {
			int stuSchool = Integer.parseInt(groupMsg.get(0).getStuSchool());
			int stuGrade = Integer.parseInt(groupMsg.get(0).getStuGrade());
			int stuHobby = Integer.parseInt(groupMsg.get(0).getStuHobby());
			int maxScore = stuSchool + stuGrade + stuHobby;
			int groupNum = Integer.parseInt(groupMsg.get(0).getGroupNum());   		 //组数
			int groupStuNum = Integer.parseInt(groupMsg.get(0).getGroupStuNum());   //每组学生数量
			List<CampusStudent> students = stuGroupMapper.queryAllStudent();		//所有学生
			List<CampusStudent> modelStudents = new ArrayList();					//模板学生
			Random random =new Random();
			
			for(int i = 0;i < groupNum;++i)
			{
				int tmp = random.nextInt(students.size());
				CampusStudent tmpStu = students.get(tmp);
				modelStudents.add(tmpStu);
				Integer groupId = i + 1;
				stuGroupMapper.insertGroupStu(tmpStu.getUserName(), tmpStu.getStuName(), groupId.toString(), tmpStu.getStuSchool());
				students.remove(tmp);
			}
			//将同学分到组中
			for(int i = 0; i < groupNum;++i)
			{
				for(int j = 1; j < groupStuNum;++j)
				{
					int max_j = 0;
					int max_sc = 0;
					int k;
					for(k = 0; k < students.size();++k)
					{
						CampusStudent tmpStu = students.get(k);
						int sch = (tmpStu.getStuSchool().equals(modelStudents.get(i).getStuSchool()) == true? 0:1) * stuSchool;
						int gra = (tmpStu.getStuGrade().equals(modelStudents.get(i).getStuGrade()) == true? 0:1) * stuGrade;
						int hob = (tmpStu.getStuHobby().equals(modelStudents.get(i).getStuHobby()) == true? 0:1) * stuHobby;
						int score = sch + gra + hob;
						if(score == maxScore)
						{
							//Integer groupId = i + 1;
							//stuGroupMapper.insertGroupStu(tmpStu.getUserName(), tmpStu.getStuName(), groupId.toString(), tmpStu.getStuSchool());
							//students.remove(k);
							break;
						}
						if(max_sc < score)
						{
							max_j = k;
							max_sc = score;
						}
					}
					if(k == students.size())
					{
						CampusStudent tmpStu = students.get(max_j);
						Integer groupId = i + 1;
						stuGroupMapper.insertGroupStu(tmpStu.getUserName(), tmpStu.getStuName(), groupId.toString(), tmpStu.getStuSchool());
						students.remove(max_j);
					}
					else 
					{
						CampusStudent tmpStu = students.get(k);
						Integer groupId = i + 1;
						stuGroupMapper.insertGroupStu(tmpStu.getUserName(), tmpStu.getStuName(), groupId.toString(), tmpStu.getStuSchool());
						students.remove(k);
					}
				}
			}
			//处理剩余同学
			while(!students.isEmpty())
			{
				int tmp = random.nextInt(groupNum);
				CampusStudent tmpStu = students.get(0);
				Integer groupId = tmp + 1;
				stuGroupMapper.insertGroupStu(tmpStu.getUserName(), tmpStu.getStuName(), groupId.toString(), tmpStu.getStuSchool());
				students.remove(0);
			}
			this.SetSuccess();
		}
		else {
			this.SetError();
			this.responseBody.put("errorMessage", "分组失败");		
		}
		return this.responseBody;
	}
}
