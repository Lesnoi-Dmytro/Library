package org.nerdy.soft.library.repositiry;

import org.nerdy.soft.library.data.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository
		extends JpaRepository<Member, Long> {
	Optional<Member> findByName(String name);
}
