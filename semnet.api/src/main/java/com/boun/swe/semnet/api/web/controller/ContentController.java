package com.boun.swe.semnet.api.web.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.boun.swe.semnet.commons.data.request.AddCommentRequest;
import com.boun.swe.semnet.commons.data.request.AddContentRequest;
import com.boun.swe.semnet.commons.data.request.BasicQueryRequest;
import com.boun.swe.semnet.commons.data.request.DeleteCommentRequest;
import com.boun.swe.semnet.commons.data.request.ListContentRequest;
import com.boun.swe.semnet.commons.data.response.ActionResponse;
import com.boun.swe.semnet.commons.data.response.CommentListResponse;
import com.boun.swe.semnet.commons.data.response.ContentListResponse;
import com.boun.swe.semnet.commons.data.response.CreateResponse;
import com.boun.swe.semnet.commons.data.response.GetContentResponse;
import com.boun.swe.semnet.commons.data.response.LikeResponse;
import com.boun.swe.semnet.commons.exception.SemNetException;
import com.boun.swe.semnet.commons.type.ErrorCode;
import com.boun.swe.semnet.sevices.service.ContentService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import shaded.org.apache.commons.io.IOUtils;

@RestController
@Api(value = "content", description = "Content service")
@RequestMapping("/v1/content")
public class ContentController {

	private final static Logger logger = LoggerFactory.getLogger(ContentController.class);
	
    @Autowired
    private ContentService contentService;

    @ApiOperation(value="Create Content")
    @RequestMapping(value="create", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody ActionResponse createContent(@RequestBody AddContentRequest request) {

    	try{
    		return contentService.create(request);
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running createContent service, code->" + e.getErrorCode());
    		
    		return new ActionResponse(e.getErrorCode(), e.getErrors());
    	}
    }
    
    @ApiOperation(value="Upload Content")
    @RequestMapping(value="upload", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody ActionResponse uploadContent(@RequestParam("file") MultipartFile file, @RequestParam("contentId")String contentId, @RequestParam("authToken")String authToken) {

    	try{
    		return contentService.upload(authToken, file.getBytes(), file.getOriginalFilename(), contentId);
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running createContent service, code->" + e.getErrorCode());
    		
    		return new ActionResponse(e.getErrorCode(), e.getErrors());
    	}catch (IOException e) {
    		
    		logger.error("IO Error occured while running createContent service", e);
    		
    		return new ActionResponse(ErrorCode.CORRUPTED_IMAGE, null);
		}
    }
    
	@ApiOperation(value = "Download Content")
	@RequestMapping(value = "downloadContent", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody
	void downloadContent(@RequestParam("contentId")String contentId, @RequestParam("authToken")String authToken,  HttpServletResponse response) {

		try {

			byte[] image =  contentService.downloadContent(authToken, contentId);

			response.setContentType("application/force-download");
			response.setContentLength((int) image.length);
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setHeader("Content-Disposition","attachment; filename=\"" + "ContentImage" +"\"");

			IOUtils.copy(new ByteArrayInputStream(image), response.getOutputStream());
			
		}catch (SemNetException e) {
    		logger.error("Error occured while running createContent service, code->" + e.getErrorCode());
		} catch (Exception e) {
			logger.error("IO Error occured while running createContent service", e);
		}
	}
	
    @ApiOperation(value="Get Content")
    @RequestMapping(value="get", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody GetContentResponse getContent(@RequestBody BasicQueryRequest request) {

    	try{
    		return contentService.get(request);
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running getContent service, code->" + e.getErrorCode());
    		
    		return new GetContentResponse(e.getErrorCode(), e.getErrors());
    	}
    }
    
    @ApiOperation(value="List Content")
    @RequestMapping(value="list", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody ContentListResponse listContent(@RequestBody ListContentRequest request) {

    	try{
    		return contentService.list(request);
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running listContent service, code->" + e.getErrorCode());
    		
    		return new ContentListResponse(e.getErrorCode(), e.getErrors());
    	}
    }
    
    @ApiOperation(value="List Comments")
    @RequestMapping(value="listComments", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody CommentListResponse listComments(@RequestBody BasicQueryRequest request) {

    	try{
    		return contentService.listComments(request);
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running listComment service, code->" + e.getErrorCode());
    		
    		return new CommentListResponse(e.getErrorCode(), e.getErrors());
    	}
    }
    
    @ApiOperation(value="Like Content")
    @RequestMapping(value="like", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody LikeResponse likeContent(@RequestBody BasicQueryRequest request) {

    	try{
    		return contentService.like(request);
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running likeContent service, code->" + e.getErrorCode());
    		
    		return new LikeResponse(e.getErrorCode(), e.getErrors());
    	}
    }
    
    @ApiOperation(value="Unlike Content")
    @RequestMapping(value="unlike", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody LikeResponse unlikeContent(@RequestBody BasicQueryRequest request) {

    	try{
    		return contentService.unLike(request);
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running unLikeContent service, code->" + e.getErrorCode());
    		
    		return new LikeResponse(e.getErrorCode(), e.getErrors());
    	}
    }
    
    @ApiOperation(value="Add Comment to a Content")
    @RequestMapping(value="addComment", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody CreateResponse addComment(@RequestBody AddCommentRequest request) {

    	try{
    		return contentService.addComment(request);
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running addComment service, code->" + e.getErrorCode());
    		
    		return new CreateResponse(e.getErrorCode(), e.getErrors());
    	}
    }
    
    @ApiOperation(value="Remove Comment from a Content")
    @RequestMapping(value="removeComment", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody ActionResponse removeComment(@RequestBody DeleteCommentRequest request) {

    	try{
    		return contentService.deleteComment(request);
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running removeComment service, code->" + e.getErrorCode());
    		
    		return new ContentListResponse(e.getErrorCode(), e.getErrors());
    	}
    }
}
