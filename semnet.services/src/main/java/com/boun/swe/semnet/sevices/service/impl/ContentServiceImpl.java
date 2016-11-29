package com.boun.swe.semnet.sevices.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boun.swe.semnet.commons.data.request.AddCommentRequest;
import com.boun.swe.semnet.commons.data.request.AddContentRequest;
import com.boun.swe.semnet.commons.data.request.BaseRequest;
import com.boun.swe.semnet.commons.data.request.BasicQueryRequest;
import com.boun.swe.semnet.commons.data.request.DeleteCommentRequest;
import com.boun.swe.semnet.commons.data.request.ListContentRequest;
import com.boun.swe.semnet.commons.data.response.ActionResponse;
import com.boun.swe.semnet.commons.data.response.ContentListResponse;
import com.boun.swe.semnet.commons.data.response.ContentObj;
import com.boun.swe.semnet.commons.data.response.CreateResponse;
import com.boun.swe.semnet.commons.data.response.GetContentResponse;
import com.boun.swe.semnet.commons.exception.SemNetException;
import com.boun.swe.semnet.commons.type.ErrorCode;
import com.boun.swe.semnet.commons.util.KeyUtils;
import com.boun.swe.semnet.sevices.db.manager.ContentManager;
import com.boun.swe.semnet.sevices.db.manager.ImagePersistencyManager;
import com.boun.swe.semnet.sevices.db.model.Comment;
import com.boun.swe.semnet.sevices.db.model.Content;
import com.boun.swe.semnet.sevices.db.model.Friendship;
import com.boun.swe.semnet.sevices.db.model.User;
import com.boun.swe.semnet.sevices.service.BaseService;
import com.boun.swe.semnet.sevices.service.ContentService;

@Service
public class ContentServiceImpl extends BaseService implements ContentService{

	@Autowired
	private ContentManager contentRepository;
	
	@Autowired
	private ImagePersistencyManager imagePersistencyManager;
	
	@Override
	public CreateResponse create(AddContentRequest request){
		validate(request);
		
		User authenticatedUser = userManager.login(request.getAuthToken(), null);
		
		Content content = new Content();
		content.setDescription(request.getDescription());
		content.setCreationDate(new Date());
		content.setOwner(authenticatedUser);
		
		authenticatedUser.getContents().add(content);
		
		contentRepository.merge(content);
		userManager.merge(authenticatedUser);
		
		return new CreateResponse(ErrorCode.SUCCESS, content.getId());
	}
	
	@Override
	public GetContentResponse get(BasicQueryRequest request){
		validate(request);
		
		User authenticatedUser = userManager.login(request.getAuthToken(), null);

		Content content = contentRepository.findById(request.getId());
		if(content == null){
			throw new SemNetException(ErrorCode.CONTENT_NOT_FOUND);
		}
		
		if(!content.getOwner().getId().equals(authenticatedUser.getId())){
			throw new SemNetException(ErrorCode.CONTENT_DOES_NOT_BELONG_TO_YOU);
		}
		
		GetContentResponse resp = new GetContentResponse(ErrorCode.SUCCESS);
		resp.setContentDetails(content.getId(), content.getDescription(), content.getCreationDate(), content.getOwner().getId());
		
		if(content.getComments() != null && !content.getComments().isEmpty()){
			for (Comment c : content.getComments()) {
				resp.addToCommentList(c.getId(), c.getDescription(), c.getCreationDate(), c.getOwner().getId(), c.getOwner().getUsername());		
			}
		}
		
		if(content.getLikers() != null && !content.getLikers().isEmpty()){
			for (User liker : content.getLikers()) {
				resp.addToLikerList(liker.getId(), liker.getUsername());
			}
		}
		
		return resp;
	}
	
	@Override
	public ContentListResponse list(ListContentRequest request){
		validate(request);
		
		ContentListResponse resp = new ContentListResponse(ErrorCode.SUCCESS);
		
		User authenticatedUser = userManager.login(request.getAuthToken(), null);
		
		List<Content> contentList = getContentList(request, authenticatedUser);
		if(contentList == null || contentList.isEmpty()){
			return resp;
		}

		for (Content content : contentList) {
			ContentObj obj = new ContentObj(content.getId(), content.getDescription(), content.getCreationDate(), content.getOwner().getId());
			
			if(content.getComments() != null && !content.getComments().isEmpty()){
				for (Comment c : content.getComments()) {
					obj.addToCommentList(c.getId(), c.getDescription(), c.getCreationDate(), c.getOwner().getId(), c.getOwner().getUsername());		
				}
			}
			
			if(content.getLikers() != null && !content.getLikers().isEmpty()){
				for (User liker : content.getLikers()) {
					obj.addToLikerList(liker.getId(), liker.getUsername());
				}
			}
			
			resp.addContent(obj);
		}
		
		return resp;
	}
	
	@Override
	public ActionResponse like(BasicQueryRequest request){
		validate(request);
		
		Content content = contentRepository.findById(request.getId());
		if(content == null){
			throw new SemNetException(ErrorCode.CONTENT_NOT_FOUND);
		}

		User authenticatedUser = userManager.login(request.getAuthToken(), null);
		
		List<User> userList = content.getLikers();
		if(userList == null){
			userList = new ArrayList<>();
		}
		
		if(!userList.contains(authenticatedUser)){
			userList.add(authenticatedUser);	
		}
		
		content.setLikeCount(userList.size());
		content.setLikers(userList);
		
		contentRepository.merge(content);
		
		return new ActionResponse(ErrorCode.SUCCESS);
	}
	
	@Override
	public ActionResponse unLike(BasicQueryRequest request){
		validate(request);
		
		Content content = contentRepository.findById(request.getId());
		if(content == null){
			throw new SemNetException(ErrorCode.CONTENT_NOT_FOUND);
		}

		User authenticatedUser = userManager.login(request.getAuthToken(), null);
		
		List<User> userList = content.getLikers();
		if(userList == null || userList.isEmpty()){
			
			content.setLikeCount(0);
			contentRepository.merge(content);
			
		}else if(userList.contains(authenticatedUser)){
			
			userList.remove(authenticatedUser);
			content.setLikeCount(userList.size());
			
			contentRepository.merge(content);
		}
		
		return new ActionResponse(ErrorCode.SUCCESS);
	}
	
	@Override
	public ActionResponse addComment(AddCommentRequest request){
		validate(request);
		
		Content content = contentRepository.findById(request.getContentId());
		if(content == null){
			throw new SemNetException(ErrorCode.CONTENT_NOT_FOUND);
		}

		User authenticatedUser = userManager.login(request.getAuthToken(), null);
		
		Comment comment = new Comment();
		comment.setCreationDate(new Date());
		comment.setDescription(request.getDescription());
		comment.setOwner(authenticatedUser);
		
		List<Comment> commentList = content.getComments();
		if(commentList == null){
			commentList = new ArrayList<>();
		}
		comment.setId(KeyUtils.currentTimeUUID().toString());
		
		commentList.add(comment);
		
		content.setComments(commentList);
		
		contentRepository.merge(content);
		
		return new ActionResponse(ErrorCode.SUCCESS);
	}
	
	@Override
	public ActionResponse deleteComment(DeleteCommentRequest request){
		validate(request);
		
		Content content = contentRepository.findById(request.getContentId());
		if(content == null){
			throw new SemNetException(ErrorCode.CONTENT_NOT_FOUND);
		}

		User authenticatedUser = userManager.login(request.getAuthToken(), null);
		
		List<Comment> commentList = content.getComments();
		if(commentList == null  || commentList.isEmpty()){
			return new ActionResponse(ErrorCode.SUCCESS);
		}
		
		Optional<Comment> commentObj = commentList.stream().filter(x -> x.getOwner().getId().equals(request.getCommentId())).findFirst();
		if(!commentObj.isPresent()){
			return new ActionResponse(ErrorCode.SUCCESS);
		}
		
		Comment comment = commentObj.get();
		if(!comment.getOwner().getId().equals(authenticatedUser.getId())){
			throw new SemNetException(ErrorCode.COMMENT_DOES_NOT_BELONG_TO_YOU);
		}
		
		commentList.remove(content);
		content.setComments(commentList);
		
		contentRepository.merge(content);
		
		return new ActionResponse(ErrorCode.SUCCESS);
	}
	
	private List<Content> getContentList(ListContentRequest request, User authenticatedUser){

		switch (request.getType()) {
		case RECENT:
			return contentRepository.findLatestContents();
		case POPULAR:
			return contentRepository.findPopularContents();
		case SPECIFIED:
			User user = userManager.findById(request.getUserId());
			return user != null ? user.getContents() : new ArrayList<>();
		case FRIEND:
			List<Content> resultList = new ArrayList<>();
			
			List<Friendship> friendList = authenticatedUser.getFriendList();
			friendList.stream().filter(x -> x.isActive()).forEach(y -> resultList.addAll(y.getUser().getContents()));
			return resultList;
		default:
			break;
		}
		return new ArrayList<>();
	}

	@Override
	public ActionResponse upload(String authToken, byte[] image, String filename, String contentId) {
		
		BaseRequest request = new BaseRequest();
		request.setAuthToken(authToken);
		
		validate(request);
		
		Content content = contentRepository.findById(contentId);
		if(content == null){
			throw new SemNetException(ErrorCode.CONTENT_NOT_FOUND);
		}
		
		imagePersistencyManager.saveImage(contentId, image);
		
		return new ActionResponse(ErrorCode.SUCCESS);
	}
	
	@Override
	public byte[] downloadContent(String authToken, String contentId) {
		
		BaseRequest request = new BaseRequest();
		request.setAuthToken(authToken);
		
		validate(request);
		
		return imagePersistencyManager.getImage(contentId);
	}
}