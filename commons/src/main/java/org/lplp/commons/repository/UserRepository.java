package org.lplp.commons.repository;

import org.lplp.commons.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

	// 不需要实现此方法，Spring会自动实现（利用动态代理技术实现）
	// 最终生成的SQL语句 : select * from wx_user where open_id = ?
	// 并且会自动把查询结果转换为User类的实例
	User findByOpenId(String openId);
}
