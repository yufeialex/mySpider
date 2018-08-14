package com.yufei.vo;

public final class DetailSeed {

	private String link; //详情链接
	private String name; //片名

	public DetailSeed() {
	}

	public DetailSeed(String id, String name){
		this.link = id;
		this.name = name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString(){
		return "{link: " + link + ", name: " + name + "}";
	}
	
}
