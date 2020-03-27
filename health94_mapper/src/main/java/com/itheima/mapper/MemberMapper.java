package com.itheima.mapper;

import com.itheima.pojo.Member;
import org.apache.ibatis.annotations.Param;

public interface MemberMapper {
    Member findByTelephone(@Param("telephone")String telephone);

    void add(Member member);

    Integer findMemberCountByMonth(@Param("dateBegin")String dateBegin,@Param("dateEnd") String dateEnd);

    Integer findTodayNewMember(@Param("today")String today);

    Integer findTotalMember();

    Integer findMemberCountAfterOrderDate(@Param("orderDate")String orderDate);
}
