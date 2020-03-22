package com.duongll.succotask.api;

import java.util.List;

import com.duongll.succotask.entity.Team;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TeamApi {

    @GET("api/v1/team/{id}/detail")
    Call<Team> getTeamInfoById(@Path("id") Long id);

    @GET("api/v1/team/teams")
    Call<List<Team>> listAllTeam();
}
