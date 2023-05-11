package ibf2022.tfip.csf.day39workshopmarvelapi.repositories;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import ibf2022.tfip.csf.day39workshopmarvelapi.model.Comment;

@Repository
public class CharCommentsRepository {
    @Autowired
    private MongoTemplate template;

    private static final String COMMENTS_COL = "comments";

    public Comment insertComment(Comment c){
        return this.template.insert(c,      COMMENTS_COL);
    }

    public List<Comment> getAllComments(String charId){
        Pageable pageable = PageRequest.of(0,10);
        Query commentsDynamicQuery = new Query() 
            .addCriteria(Criteria.where("charId").is(charId))
            .with((pageable));

        List<Comment> filterComments = template.find(commentsDynamicQuery, Comment.class, COMMENTS_COL);

        Page<Comment> commentPage = PageableExecutionUtils.getPage(
            filterComments, pageable, ()-> template.count(commentsDynamicQuery, Comment.class));

        return commentPage.toList();
    }
}
