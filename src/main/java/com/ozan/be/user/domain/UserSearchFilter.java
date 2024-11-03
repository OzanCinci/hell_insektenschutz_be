package com.ozan.be.user.domain;

import static java.util.Objects.nonNull;

import com.ozan.be.common.AbstractSearchFilter;
import com.ozan.be.user.QUser;
import com.querydsl.core.types.Predicate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchFilter extends AbstractSearchFilter {
  List<Role> roles;
  String email;
  String q;

  @Override
  public void buildPredicateList(List<Predicate> predicateList) {
    if (nonNull(roles) && !roles.isEmpty()) {
      predicateList.add(QUser.user.role.in(roles));
    }

    if (nonNull(email)) {
      predicateList.add(QUser.user.email.containsIgnoreCase(email));
    }

    if (nonNull(q)) {
      predicateList.add(
          QUser.user.firstName.containsIgnoreCase(q).or(QUser.user.lastName.containsIgnoreCase(q)));
    }
  }
}
