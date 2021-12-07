package ru.klimashin.ballistic.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Engine {

	private String name;
	private double thrust;

}
