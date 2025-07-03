package com.chakak.common.enums;

public enum Violation {
	CROSSWALK("횡단보도"),
	BUS_STOP("버스정류장"),
	FIRE_HYDRANT("소화전/소방시설"),
	INTERSECTION("교차로 모퉁이"),
	SIDEWALK("인도 위"),
	BIKE_LANE("자전거도로"),
	DISABLED_ZONE("장애인 주차구역"),
	STOP_LINE("정지선 침범"),
	SCHOOL_ZONE("어린이 보호구역"),
	DOUBLE_PARKING("이중주차"),
	OTHER("기타");
	
	private final String label;
	
	Violation(String label){
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
}
