package com.duongll.succotask.api;

import java.util.Date;
import java.util.List;

import com.duongll.succotask.dto.CreateTaskDto;
import com.duongll.succotask.dto.ModifyTaskDto;
import com.duongll.succotask.dto.SubmitTaskDto;
import com.duongll.succotask.dto.TaskCommentDto;
import com.duongll.succotask.entity.Task;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TaskApi {

    @GET(value = "/api/v1/user/{id}/task/list")
    Call<List<Task>> getTaskListForUser(@Path("id") Long userId);

    @GET(value = "/api/v1/user/{id}/task/history")
    Call<List<Task>> getUserHistory(@Path("id") Long userId,
                                    @Query("start")Date startDate,
                                    @Query("end")Date endDate,
                                    @Query("status") String status);

    @POST(value = "/api/v1/task/create")
    Call<CreateTaskDto> createNewTask(@Body CreateTaskDto taskDto);

    @GET(value = "/api/v1/task/{id}/approveTasks")
    Call<List<Task>> getAllTaskNeedApproveForManager(@Path("id") Long userId);

    @GET(value = "/api/v1/task/{id}/pendingTasks")
    Call<List<Task>> getPendingTaskForUser(@Path("id") Long userId);

    @PUT(value = "/api/v1/task/{id}/acceptOrDecline")
    Call<Task> acceptOrDeclineSubmitTask(@Path("id") Long taskId,
                                         @Body TaskCommentDto dto);

    @PUT(value = "/api/v1/task/status/{id}")
    Call<Task> changeTaskStatus(@Path("id") Long taskId,
                                @Query("status") String status);

    @PUT(value = "/api/v1/task/{id}/submit")
    Call<Task> submitTaskFromUser(@Path("id") Long taskId,
                                  @Body SubmitTaskDto submitTaskDTO);

    @PUT(value = "/api/v1/task/{id}/modify")
    Call<Task> modifyUserPendingTask(@Path("id") Long taskId,
                                     @Body ModifyTaskDto modifyTaskDto);
}
