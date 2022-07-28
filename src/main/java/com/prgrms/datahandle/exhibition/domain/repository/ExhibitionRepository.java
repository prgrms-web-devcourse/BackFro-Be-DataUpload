package com.prgrms.datahandle.exhibition.domain.repository;

import com.prgrms.datahandle.exhibition.domain.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {

}
