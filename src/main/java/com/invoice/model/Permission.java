package com.invoice.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "permission")
@EqualsAndHashCode
@Entity
public class Permission implements GrantedAuthority, Serializable {

	private static final long serialVersionUID = -4460602054299804896L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String description;

	@Override
	public String getAuthority() {
		return this.description;
	}

}
