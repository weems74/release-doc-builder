package com.lexmark.releasenotetests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestCommitDetail;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.PagedIterable;

public class PullRequestTest {

	/**
	 * Just experimenting with getting the values I want. Much of these would be separated
	 * into different classes in real implementation
	 */
	
	//Initiate classes
	GitHub github = null;
	
	@Test
	public void GetPullRequestTest() throws IOException {
		System.out.println("Starting test...");
	
		try {
			//use the oauth in the ~/.github file to authenticate
			github = GitHubBuilder.fromPropertyFile().build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Couldn't get github object built");
		}
		
		System.out.println("Got github object built...connecting to repo.");
		

		
		//First check to see if you can access the description
		GHRepository testrepo = github.getRepository("weems74/gitLearning");
		
		//Test rate limit (to see if authentication is working)
		System.out.println("Rate limit:" + github.getRateLimit());
		
		//GHRepository testrepo = github.getRepository("LexmarkWeb/LXK_Framework");
		System.out.println("Description of testrepo:" + testrepo.getDescription());
	
		
		//Get a list of all the PRs that are closed on this REPO		 
		List<GHPullRequest> pullRequestList = testrepo.getPullRequests(GHIssueState.CLOSED);
				 
		//iterate through the pull requests
		for (GHPullRequest currentPR:pullRequestList){
			
			System.out.println("\rPR ID:" + currentPR.getId());
			System.out.println("PR Title:" + currentPR.getTitle());
			System.out.println("PR Number of Changed Files:" + currentPR.getChangedFiles());
			
			//get the commits in each pull request
			PagedIterable<GHPullRequestCommitDetail> prCommits = currentPR.listCommits();
			
			//iterate through the commits and get the files updated
			for (GHPullRequestCommitDetail currentCommit: prCommits.asList()){
				String sha = currentCommit.getSha();
				List<org.kohsuke.github.GHCommit.File> filesUpdated = testrepo.getCommit(sha).getFiles();
				
				//iterate through the files updated and get the URL
				for (org.kohsuke.github.GHCommit.File currentFile:filesUpdated){
					
					String currentFileBlobURL = currentFile.getBlobUrl().toString();
					
					//Split the string on either section of the blob part of the path
					String[] urlSections = currentFileBlobURL.split("\\/blob\\/[^\\/]*");
					System.out.println("file path =" + urlSections[urlSections.length-1]);

				
				}
				
						
			}
				
		} 
	
/*
 * Playing with commits.
		System.out.println("Getting commits");
		
		PagedIterable<GHCommit> commit = testrepo.listCommits();
		List<GHCommit> commitList = commit.asList();
		for (GHCommit currentCommit: commitList){
			
			System.out.println("Current commit SHA:" + currentCommit.getSHA1());
			System.out.println("Current commit files:" + currentCommit.getFiles());
			//System.out.println("First file in commit:" + currentCommit.getFiles().get(0).toString());
			
			List<org.kohsuke.github.GHCommit.File> commitFileList = currentCommit.getFiles();
						
			for (GHCommit.File currentFile: commitFileList){
				System.out.println("Commit file  :" + currentFile.getFileName());
				System.out.println("Commit file:" + currentFile.getLinesAdded());
			}
					
		}*/
 
	}

}
