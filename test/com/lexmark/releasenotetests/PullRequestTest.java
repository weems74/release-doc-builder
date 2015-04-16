package com.lexmark.releasenotetests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
		
		/*		
		 * 
		 * PLAYING AROUND WITH PRs:
		 * 
		//Get a list of all the PRs that are closed on this REPO		 
		List<GHPullRequest> pullRequestList = testrepo.getPullRequests(GHIssueState.CLOSED);
				 
		//Go through each closed PR and output some information
	
		 * In reality, I would need to manage this better--iteratively grabbing all PRs and then all commits seems dumb.
		 * We'd need to have the Commit for the last prod-release tag, then get all the PRs since that last tag. 
		 * So one class would get those PRs
		 * Then you could call another class with each PR, and it could extract the files/folders affected.
		 * 
		 * What about commits that happen outside a PR? Shouldn't happen but could.
		 * 
		
		
		for (GHPullRequest currentPR:pullRequestList){
			
			System.out.println("PR ID:" + currentPR.getId());
			System.out.println("PR Title:" + currentPR.getTitle());
			System.out.println("PR Number of Changed Files:" + currentPR.getChangedFiles());
			System.out.println("PR Body:" + currentPR.getBody());
			System.out.println("PR Files affected: " + currentPR.listCommits().toString());
			
			PagedIterable<GHPullRequestCommitDetail> prCommits = currentPR.listCommits();
			
			for (GHPullRequestCommitDetail currentCommit: prCommits.asList()){
				String sha = currentCommit.getSha();
				List<org.kohsuke.github.GHCommit.File> filesUpdated = testrepo.getCommit(sha).getFiles();
				
				System.out.println("File 0 updated: " +	filesUpdated.get(0).getFileName());
				System.out.println("File 0 updated (bloburl):" + filesUpdated.get(0).getBlobUrl());
				
						
			}
				
		} 
		*/	
		
		System.out.println("Getting commits");
		
		PagedIterable<GHCommit> commit = testrepo.listCommits();
		List<GHCommit> commitList = commit.asList();
		for (GHCommit currentCommit: commitList){
			
			System.out.println("Current commit: " + currentCommit.toString());
			List<org.kohsuke.github.GHCommit.File> commitFileList = currentCommit.getFiles();
						
			for (GHCommit.File currentFile: commitFileList){
				System.out.println("Commit file:" + currentFile.getLinesAdded());
			}
					
		}
 
	}

}
