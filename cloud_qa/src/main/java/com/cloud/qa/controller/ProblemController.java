package com.cloud.qa.controller;
import java.util.Map;

import com.cloud.qa.service.ProblemService;
import com.cloud.qa.client.BaseClient;
import io.jsonwebtoken.Claims;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.cloud.qa.pojo.Problem;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/problem")
public class ProblemController {

	@Autowired
	private ProblemService problemService;

	@Autowired
	private HttpServletRequest request;

	@Resource
	private BaseClient baseClient;

	@GetMapping("/label/{labelId}")
	public Result findByLabelId(@PathVariable String labelId){
		return baseClient.findByIdt(labelId);
	}

	@GetMapping("/newList/{label}/{page}/{size}")
	public Result newList(@PathVariable String label, @PathVariable int page ,@PathVariable int size){
		Page<Problem> pageData = problemService.newList(label, page, size);
		return new Result(true, StatusCode.OK, "最新", new PageResult<Problem>(pageData.getTotalElements(), pageData.getContent()));
	}

	@GetMapping("/hotList/{label}/{page}/{size}")
	public Result hotList(@PathVariable String label, @PathVariable int page ,@PathVariable int size){
		Page<Problem> pageData = problemService.hotList(label, page, size);
		return new Result(true, StatusCode.OK, "最热", new PageResult<Problem>(pageData.getTotalElements(), pageData.getContent()));
	}

	@GetMapping("/waitList/{label}/{page}/{size}")
	public Result waitList(@PathVariable String label, @PathVariable int page ,@PathVariable int size){
		Page<Problem> pageData = problemService.waitList(label, page, size);
		return new Result(true, StatusCode.OK, "等待", new PageResult<Problem>(pageData.getTotalElements(), pageData.getContent()));
	}
	
	
	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",problemService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",problemService.findById(id));
	}


	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<Problem> pageList = problemService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Problem>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",problemService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param problem
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody Problem problem) {
		Claims claimsUser = (Claims) request.getAttribute("claims_user");
		if (claimsUser == null) {
			return new Result(false, StatusCode.ACCESSERROR,"权限不足");
		}
		problemService.add(problem);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param problem
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody Problem problem, @PathVariable String id ){
		problem.setId(id);
		problemService.update(problem);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		problemService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}
	
}
