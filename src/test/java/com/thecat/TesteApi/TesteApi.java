package com.thecat.TesteApi;

import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TesteApi extends MassaDeDados {

	@BeforeClass
	public static void urlbase() {
		RestAssured.baseURI = "https://api.thecatapi.com/v1/";
	}

	@Test
	public void cadastro() {
		Response response = 
				given().contentType("application/json")
				.body(corpoCadastro)
				.when().post("user/passwordlesssignup");
		validacao(response);
	}

	@Test
	public void votacao() {
		Response response = 
				given().contentType("application/json")
				.body(corpoVotacao)
				.when().post("votes/");
		
		validacao(response);
		
		String id = response.jsonPath().getString("id");
		vote_id = id;
		System.out.println("ID => " + id);
	}

	@Test
	public void deletaVotacao() {
		votacao();
		deletaVoto();
	}

	
	//Api apresenta erros, professor entrou em contato
	private void deletaVoto() {

		Response response = given().contentType("application/json")
				.header("x-api-key", "a2e273ea-115e-4781-a4de-ee89236e9896")
				.pathParam("vote_id", vote_id)
				.when().delete("votes/{vote_id}");

		validacao(response);

	}

	@Test
	public void executaFavoritarDeletar() {
		favoritarImg();
		deletarFavorito();
	}

	private void deletarFavorito() {

		Response response = given().contentType("application/json")
				.header("x-api-key", "8de3dffb-330e-40c0-a9e5-a1c5a1097255")
				.pathParam("favourite_id", vote_id)
				.when().delete("favourites/{favourite_id}");

		validacao(response);

	}

	private void favoritarImg() {

		Response response = 
				given().contentType("application/json")
				.header("x-api-key", "8de3dffb-330e-40c0-a9e5-a1c5a1097255")
				.body(corpoFavoritaImg)
				.when().post("favourites");

		String id = response.jsonPath().getString("id");
		vote_id = id;

		validacao(response);
	}

	public void validacao(Response response) {
		response.then()
		.statusCode(200)
		.body("message", containsString("SUCCESS"));
		
		System.out.println("Retorno da API  => " + response.body().asString());
		System.out.println("-----------------------------------");
	}
}
