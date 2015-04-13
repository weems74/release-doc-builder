package com.lexmark.releasenotetests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;
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
		
		
		try {
			//use the oauth in the ~/.github file to authenticate
			github = GitHubBuilder.fromEnvironment().build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Could not connect to Github repository");
		}
		
		//First check to see if you can access the description
		GHRepository testrepo = github.getRepository("weems74/gitLearning");
		//GHRepository testrepo = github.getRepository("LexmarkWeb/LXK_Framework");
		System.out.println("Description of testrepo:" + testrepo.getDescription());
		
			
		//Get a list of all the PRs that are closed on this REPO		 
		List<GHPullRequest> pullRequestList = testrepo.getPullRequests(GHIssueState.CLOSED);
				 
		//Go through each closed PR and output some information
		/*
		 * In reality, I would need to manage this better--iteratively grabbing all PRs and then all commits seems dumb.
		 * We'd need to have the Commit for the last prod-release tag, then get all the PRs since that last tag. 
		 * So one class would get those PRs
		 * Then you could call another class with each PR, and it could extract the files/folders affected.
		 * 
		 * What about commits that happen outside a PR? Shouldn't happen but could.
		 * 
		 */
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
				
						
			}
				
		}
				
	}

}
