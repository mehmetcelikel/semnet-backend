package com.boun.swe.semnet.sevices.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import com.boun.swe.semnet.commons.data.request.AddCommentRequest;
import com.boun.swe.semnet.commons.data.request.AddContentRequest;
import com.boun.swe.semnet.commons.data.request.BaseRequest;
import com.boun.swe.semnet.commons.data.request.BasicQueryRequest;
import com.boun.swe.semnet.commons.data.request.DeleteCommentRequest;
import com.boun.swe.semnet.commons.data.request.ListContentRequest;
import com.boun.swe.semnet.commons.data.response.ActionResponse;
import com.boun.swe.semnet.commons.data.response.CommentListResponse;
import com.boun.swe.semnet.commons.data.response.ContentListResponse;
import com.boun.swe.semnet.commons.data.response.ContentObj;
import com.boun.swe.semnet.commons.data.response.CreateResponse;
import com.boun.swe.semnet.commons.data.response.GetContentResponse;
import com.boun.swe.semnet.commons.data.response.LikeResponse;
import com.boun.swe.semnet.commons.exception.SemNetException;
import com.boun.swe.semnet.commons.type.ErrorCode;
import com.boun.swe.semnet.commons.util.KeyUtils;
import com.boun.swe.semnet.sevices.db.manager.ContentManager;
import com.boun.swe.semnet.sevices.db.manager.ImagePersistencyManager;
import com.boun.swe.semnet.sevices.db.model.Comment;
import com.boun.swe.semnet.sevices.db.model.Content;
import com.boun.swe.semnet.sevices.db.model.Friendship;
import com.boun.swe.semnet.sevices.db.model.TaggedEntity;
import com.boun.swe.semnet.sevices.db.model.User;
import com.boun.swe.semnet.sevices.service.ContentService;
import com.boun.swe.semnet.sevices.service.TagService;

@Service
public class ContentServiceImpl extends BaseTaggedService implements ContentService{

	private final static Logger logger = LoggerFactory.getLogger(ContentServiceImpl.class);
	
	@Autowired
	private ContentManager contentManager;
	
	@Autowired
	TagService tagService;
	
	@Autowired
	private ImagePersistencyManager imagePersistencyManager;
	
	@Override
	public CreateResponse create(AddContentRequest request){
		validate(request);
		
		User authenticatedUser = userManager.login(request.getAuthToken(), null);
		authenticatedUser = userManager.findById(authenticatedUser.getId());
		
		Content content = new Content();
		content.setDescription(request.getDescription());
		content.setCreationDate(new Date());
		content.setOwnerId(authenticatedUser.getId());
		content.setHasImage(request.isHasImage());
		content.setPosition(mapPosition(request.getLongitude(), request.getLatitude()));
		contentManager.merge(content);
		
		authenticatedUser.getContents().add(content.getId());
		
		userManager.merge(authenticatedUser);
		
		return new CreateResponse(ErrorCode.SUCCESS, content.getId());
	}
	
	@Override
	public GetContentResponse get(BasicQueryRequest request){
		validate(request);
		
		User authenticatedUser = userManager.login(request.getAuthToken(), null);

		Content content = contentManager.findById(request.getId());
		if(content == null){
			throw new SemNetException(ErrorCode.CONTENT_NOT_FOUND);
		}
		
		if(!content.getOwnerId().equals(authenticatedUser.getId())){
			throw new SemNetException(ErrorCode.CONTENT_DOES_NOT_BELONG_TO_YOU);
		}
		
		User owner = userManager.findById(content.getOwnerId());
		
		GetContentResponse resp = new GetContentResponse(ErrorCode.SUCCESS);
		resp.setContentDetails(content.getId(), content.getDescription(), content.getCreationDate(), content.getOwnerId(), owner.getUsername(), content.isHasImage(), content.getLikeCount());
		
		if(content.getLikers() == null || content.getLikers().isEmpty()){
			return resp;
		}
		
		for (String likerId : content.getLikers()) {
			User liker = userManager.findById(likerId);
			if(liker == null){
				continue;
			}
			resp.addToLikerList(liker.getId(), liker.getUsername());
		}
		
		return resp;
	}
	
	@Override
	public ContentListResponse list(ListContentRequest request){
		validate(request);
		
		ContentListResponse resp = new ContentListResponse(ErrorCode.SUCCESS);
		
		User authenticatedUser = userManager.login(request.getAuthToken(), null);
		authenticatedUser = userManager.findById(authenticatedUser.getId());
		
		List<Content> contentList = getContentList(request, authenticatedUser);
		if(contentList == null || contentList.isEmpty()){
			return resp;
		}

		for (Content content : contentList) {
			
			User owner = userManager.findById(content.getOwnerId());
			
			ContentObj obj = new ContentObj(content.getId(), content.getDescription(), content.getCreationDate(), owner.getId(), owner.getUsername(), content.isHasImage(), content.getLikeCount());
			
			if(content.getLikers() != null && !content.getLikers().isEmpty()){
				for (String likerId : content.getLikers()) {
					User liker = userManager.findById(likerId);
					if(liker == null){
						continue;
					}
					obj.addToLikerList(liker.getId(), liker.getUsername());
				}
			}
			obj.setTagList(content.getTagList());
			
			resp.addContent(obj);
		}
		
		return resp;
	}
	
	@Override
	public CommentListResponse listComments(BasicQueryRequest request){
		validate(request);
		
		CommentListResponse resp = new CommentListResponse(ErrorCode.SUCCESS);
		
		User authenticatedUser = userManager.login(request.getAuthToken(), null);
		authenticatedUser = userManager.findById(authenticatedUser.getId());
		
		Content content = contentManager.findById(request.getId());
		if(content == null){
			throw new SemNetException(ErrorCode.CONTENT_NOT_FOUND);
		}
		
		List<Comment> commentList = content.getComments();
		if(commentList == null || commentList.isEmpty()){
			return resp;
		}
		
		for (Comment comment : commentList) {
			
			User commentOwner = userManager.findById(comment.getOwnerId());
			
			resp.addComment(comment.getId(), comment.getDescription(), comment.getOwnerId(), comment.getCreationDate(), commentOwner.getUsername());
		}
		
		return resp;
	}
	
	@Override
	public LikeResponse like(BasicQueryRequest request){
		validate(request);
		
		Content content = contentManager.findById(request.getId());
		if(content == null){
			throw new SemNetException(ErrorCode.CONTENT_NOT_FOUND);
		}

		User authenticatedUser = userManager.login(request.getAuthToken(), null);
		authenticatedUser = userManager.findById(authenticatedUser.getId());
		
		List<String> userList = content.getLikers();
		if(userList == null){
			userList = new ArrayList<>();
		}
		
		if(!userList.contains(authenticatedUser.getId())){
			userList.add(authenticatedUser.getId());	
		}
		
		content.setLikeCount(userList.size());
		content.setLikers(userList);
		
		contentManager.merge(content);
		
		return new LikeResponse(ErrorCode.SUCCESS, content.getLikeCount());
	}
	
	@Override
	public LikeResponse unLike(BasicQueryRequest request){
		validate(request);
		
		Content content = contentManager.findById(request.getId());
		if(content == null){
			throw new SemNetException(ErrorCode.CONTENT_NOT_FOUND);
		}

		User authenticatedUser = userManager.login(request.getAuthToken(), null);
		authenticatedUser = userManager.findById(authenticatedUser.getId());
		
		List<String> userList = content.getLikers();
		if(userList == null || userList.isEmpty()){
			
			content.setLikeCount(0);
			contentManager.merge(content);
						
		}else if(userList.contains(authenticatedUser.getId())){
			
			userList.remove(authenticatedUser.getId());
			content.setLikeCount(userList.size());
			content.setLikers(userList);
			
			contentManager.merge(content);
		}
		
		return new LikeResponse(ErrorCode.SUCCESS, content.getLikeCount());
	}
	
	@Override
	public CreateResponse addComment(AddCommentRequest request){
		validate(request);
		
		Content content = contentManager.findById(request.getContentId());
		if(content == null){
			throw new SemNetException(ErrorCode.CONTENT_NOT_FOUND);
		}

		User authenticatedUser = userManager.login(request.getAuthToken(), null);
		
		Comment comment = new Comment();
		comment.setCreationDate(new Date());
		comment.setDescription(request.getDescription());
		comment.setOwnerId(authenticatedUser.getId());
		
		List<Comment> commentList = content.getComments();
		if(commentList == null){
			commentList = new ArrayList<>();
		}
		comment.setId(KeyUtils.currentTimeUUID().toString());
		
		commentList.add(comment);
		
		content.setComments(commentList);
		
		contentManager.merge(content);
		
		return new CreateResponse(ErrorCode.SUCCESS, comment.getId());
	}
	
	@Override
	public ActionResponse deleteComment(DeleteCommentRequest request){
		validate(request);
		
		Content content = contentManager.findById(request.getContentId());
		if(content == null){
			throw new SemNetException(ErrorCode.CONTENT_NOT_FOUND);
		}

		User authenticatedUser = userManager.login(request.getAuthToken(), null);
		
		List<Comment> commentList = content.getComments();
		if(commentList == null  || commentList.isEmpty()){
			return new ActionResponse(ErrorCode.SUCCESS);
		}
		
		Optional<Comment> commentObj = commentList.stream().filter(x -> x.getOwnerId().equals(request.getCommentId())).findFirst();
		if(!commentObj.isPresent()){
			return new ActionResponse(ErrorCode.SUCCESS);
		}
		
		Comment comment = commentObj.get();
		if(!comment.getOwnerId().equals(authenticatedUser.getId())){
			throw new SemNetException(ErrorCode.COMMENT_DOES_NOT_BELONG_TO_YOU);
		}
		
		commentList = getDiffCommentList(commentList, comment);
		content.setComments(commentList);
		
		contentManager.merge(content);
		
		return new ActionResponse(ErrorCode.SUCCESS);
	}
	
	private List<Content> getContentList(ListContentRequest request, User authenticatedUser){

		switch (request.getType()) {
		case LOCATION:
			return contentManager.findByPositionNear(new Point(request.getLongitude(), request.getLatitude()) , new Distance(100, Metrics.KILOMETERS) );
		case RECENT:
			return contentManager.findLatestContents();
		case POPULAR:
			return contentManager.findPopularContents();
		case SPECIFIED:
			User user = userManager.findById(request.getUserId());

			List<Content> contentList = new ArrayList<>();
			if(user != null){
				
				List<String> contentIdList = user.getContents();
				
				for (String contentId : contentIdList) {
					Content content = contentManager.findById(contentId);
					if(content == null){
						continue;
					}
					contentList.add(content);
				}
			}
			
			return contentList;
			
		case FRIEND:
			List<Content> resultList = new ArrayList<>();
			
			logger.info("contentList->" + authenticatedUser.getId() + ", FriendCount->" + authenticatedUser.getFriendList().size());
			
			List<Friendship> friendList = authenticatedUser.getFriendList();
			for (Friendship friendship : friendList) {
				
				if(!friendship.isActive()){
					continue;
				}
				User f = userManager.findById(friendship.getUserId());
				
				List<String> contentIdList = f.getContents();
				
				for (String contentId : contentIdList) {
					Content content = contentManager.findById(contentId);
					if(content == null){
						continue;
					}
					resultList.add(content);
				}
			}
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
		
		Content content = contentManager.findById(contentId);
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
	
	public List<User> getDiffUserList(List<User> userList, User user){
		List<User> newList = new ArrayList<>();
		for (User u : userList) {
			if(!u.getId().equals(user.getId())){
				newList.add(u);
			}
		}
		return newList;
	}
	
	public List<Comment> getDiffCommentList(List<Comment> commentList, Comment comment){
		List<Comment> newList = new ArrayList<>();
		for (Comment u : commentList) {
			if(!u.getId().equals(comment.getId())){
				newList.add(u);
			}
		}
		return newList;
	}
	
	private double[] mapPosition(double longitude, double latitude){
		double[] position = new double[2];
		position[0] = longitude;
		position[1] = latitude;
		return position;
	}

	@Override
	protected TagService getTagService() {
		return tagService;
	}

	@Override
	public TaggedEntity findById(String entityId) {
		return contentManager.findById(entityId);
	}

	@Override
	public void save(TaggedEntity entity) {
		contentManager.merge((Content)entity);
	}
}