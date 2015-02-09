package com.lakshya.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
 

public class Project {

	private String description;
	private String video_url;
	private double percentage_pledged;
	private int pledged_amount;
	private String author_name;
	private int id;
	private List<String> image_urls;
	private int total_backers;
	private int goal;
	private String title;
	private String summary;
	private String primary_picture_url;
	private int days_remaining;
	private String team;
	private String risks_and_challenges;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description
	 * The description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 * @return
	 * The videoUrl
	 */
	public String getVideoUrl() {
		return video_url;
	}

	/**
	 * 
	 * @param videoUrl
	 * The video_url
	 */
	public void setVideoUrl(String videoUrl) {
		this.video_url = videoUrl;
	}

	/**
	 * 
	 * @return
	 * The percentagePledged
	 */
	public Double getPercentagePledged() {
		return percentage_pledged;
	}

	/**
	 * 
	 * @param percentagePledged
	 * The percentage_pledged
	 */
	public void setPercentagePledged(Double percentagePledged) {
		this.percentage_pledged = percentagePledged;
	}

	/**
	 * 
	 * @return
	 * The pledgedAmount
	 */
	public Integer getPledgedAmount() {
		return pledged_amount;
	}

	/**
	 * 
	 * @param pledgedAmount
	 * The pledged_amount
	 */
	public void setPledgedAmount(Integer pledgedAmount) {
		this.pledged_amount = pledgedAmount;
	}

	/**
	 * 
	 * @return
	 * The authorName
	 */
	public String getAuthorName() {
		return author_name;
	}

	/**
	 * 
	 * @param authorName
	 * The author_name
	 */
	public void setAuthorName(String authorName) {
		this.author_name = authorName;
	}

	/**
	 * 
	 * @return
	 * The id
	 */

	public Integer getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 * The id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 
	 * @return
	 * The imageUrls
	 */
	public List<String> getImageUrls() {
		return image_urls;
	}

	/**
	 * 
	 * @param imageUrls
	 * The image_urls
	 */
	public void setImageUrls(List<String> imageUrls) {
		this.image_urls = imageUrls;
	}

	/**
	 * 
	 * @return
	 * The totalBackers
	 */
	public Integer getTotalBackers() {
		return total_backers;
	}

	/**
	 * 
	 * @param totalBackers
	 * The total_backers
	 */
	public void setTotalBackers(Integer totalBackers) {
		this.total_backers = totalBackers;
	}

	/**
	 * 
	 * @return
	 * The goal
	 */
	public Integer getGoal() {
		return goal;
	}

	/**
	 * 
	 * @param goal
	 * The goal
	 */
	public void setGoal(Integer goal) {
		this.goal = goal;
	}

	/**
	 * 
	 * @return
	 * The title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 
	 * @param title
	 * The title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 
	 * @return
	 * The summary
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * 
	 * @param summary
	 * The summary
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 * 
	 * @return
	 * The primaryPictureUrl
	 */
	public String getPrimaryPictureUrl() {
		return primary_picture_url;
	}

	/**
	 * 
	 * @param primaryPictureUrl
	 * The primary_picture_url
	 */
	public void setPrimaryPictureUrl(String primaryPictureUrl) {
		this.primary_picture_url = primaryPictureUrl;
	}

	/**
	 * 
	 * @return
	 * The daysRemaining
	 */
	public Integer getDaysRemaining() {
		return days_remaining;
	}

	/**
	 * 
	 * @param daysRemaining
	 * The days_remaining
	 */
	public void setDaysRemaining(Integer daysRemaining) {
		this.days_remaining = daysRemaining;
	}

	/**
	 * 
	 * @return
	 * The team
	 */
	public String getTeam() {
		return team;
	}

	/**
	 * 
	 * @param team
	 * The team
	 */
	public void setTeam(String team) {
		this.team = team;
	}

	/**
	 * 
	 * @return
	 * The risksAndChallenges
	 */
	public String getRisksAndChallenges() {
		return risks_and_challenges;
	}

	/**
	 * 
	 * @param risksAndChallenges
	 * The risks_and_challenges
	 */
	public void setRisksAndChallenges(String risksAndChallenges) {
		this.risks_and_challenges = risksAndChallenges;
	}

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}