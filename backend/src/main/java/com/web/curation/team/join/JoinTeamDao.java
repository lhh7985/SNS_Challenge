package com.web.curation.team.join;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.curation.member.Member;
import com.web.curation.team.Team;
import com.web.curation.team.TeamDto;

public interface JoinTeamDao extends JpaRepository<JoinTeam, Integer> {

	public List<JoinTeam> findJoinTeamByMember(Member member);
	public Optional<JoinTeam> findByMemberAndTeam(Member member, Team team);
}
