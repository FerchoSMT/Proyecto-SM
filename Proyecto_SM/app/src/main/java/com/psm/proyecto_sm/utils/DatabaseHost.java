package com.psm.proyecto_sm.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.psm.proyecto_sm.activities.MainActivity;
import com.psm.proyecto_sm.activities.PostActivity;
import com.psm.proyecto_sm.activities.ProfileActivity;
import com.psm.proyecto_sm.activities.RegisterActivity;
import com.psm.proyecto_sm.adapters.RepliesAdapter;
import com.psm.proyecto_sm.models.Post;
import com.psm.proyecto_sm.models.Reply;
import com.psm.proyecto_sm.models.User;

import org.json.JSONObject;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHost {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createUser(Context context, String url, User user) {

        String encodedString, strEncodedImage;

        if (user.getProfile_picture() != null) {
            encodedString = Base64.getEncoder().encodeToString(user.getProfile_picture());
            strEncodedImage = "data:image/png;base64," + encodedString;
        }
        else {
            strEncodedImage = "";
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        long user_id = -1;

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            user_id = jsonObject.getLong("idUserCreated");
                        } catch (Exception e) {
                            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                        } finally {
                            if (user_id > 0) {
                                DataManager.INSTANCE.setUserId(user_id);
                                DataManager.INSTANCE.setUserProfilePic(user.getProfile_picture());

                                Intent intent = new Intent(context, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                context.startActivity(intent);
                                ((Activity)context).finish();
                            }
                            else {
                                Toast.makeText(context, "Error al registrar usuario", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", user.getName());
                params.put("email", user.getEmail());
                params.put("password", user.getPassword());
                params.put("phone", user.getPhone());
                params.put("address", user.getAddress());
                params.put("profile_picture", strEncodedImage);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateUser(Context context, String url, User user) {

        String encodedString = Base64.getEncoder().encodeToString(user.getProfile_picture());
        String strEncodedImage = "data:image/png;base64," + encodedString;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        long updated = 0;

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            updated = jsonObject.getLong("updated");
                        } catch (Exception e) {
                            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                        } finally {
                            if (updated > 0) {
                                DataManager.INSTANCE.setUserProfilePic(user.getProfile_picture());

                                Intent intent = new Intent(context, ProfileActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                context.startActivity(intent);
                                ((Activity)context).finish();
                            }
                            else {
                                Toast.makeText(context, "Error al editar usuario", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", user.getId_user().toString());
                params.put("name", user.getName());
                params.put("email", user.getEmail());
                params.put("password", user.getPassword());
                params.put("phone", user.getPhone());
                params.put("address", user.getAddress());
                params.put("profile_picture", strEncodedImage);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createPost(Context context, String url, Post post) {

        String encodedStringA, encodedStringB, strEncodedImageA, strEncodedImageB;

        if (post.getImageA() != null) {
            encodedStringA = Base64.getEncoder().encodeToString(post.getImageA());
            strEncodedImageA = "data:image/png;base64," + encodedStringA;
        } else {
            strEncodedImageA = "";
        }

        if (post.getImageB() != null) {
            encodedStringB = Base64.getEncoder().encodeToString(post.getImageB());
            strEncodedImageB = "data:image/png;base64," + encodedStringB;
        } else {
            strEncodedImageB = "";
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        long post_id = -1;

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            post_id = jsonObject.getLong("idPostCreated");
                        } catch (Exception e) {
                            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                        } finally {
                            if (post_id > 0) {
                                Intent intent = new Intent(context, PostActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                intent.putExtra("IdPost", post_id);
                                context.startActivity(intent);
                                ((Activity)context).finish();
                            }
                            else {
                                Toast.makeText(context, "Error al crear post", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("title", post.getTitle());
                params.put("content", post.getContent());
                params.put("id_user", post.getId_user().toString());
                params.put("imageA", strEncodedImageA);
                params.put("imageB", strEncodedImageB);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updatePost(Context context, String url, Post post) {

        String encodedStringA, encodedStringB, strEncodedImageA, strEncodedImageB;

        if (post.getImageA() != null) {
            encodedStringA = Base64.getEncoder().encodeToString(post.getImageA());
            strEncodedImageA = "data:image/png;base64," + encodedStringA;
        } else {
            strEncodedImageA = "";
        }

        if (post.getImageB() != null) {
            encodedStringB = Base64.getEncoder().encodeToString(post.getImageB());
            strEncodedImageB = "data:image/png;base64," + encodedStringB;
        } else {
            strEncodedImageB = "";
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        long updated = 0;

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            updated = jsonObject.getLong("updated");
                        } catch (Exception e) {
                            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                        } finally {
                            if (updated > 0) {
                                Intent intent = new Intent(context, PostActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                intent.putExtra("IdPost", post.getId_post());
                                context.startActivity(intent);
                                ((Activity)context).finish();
                            }
                            else {
                                Toast.makeText(context, "Error al editar post", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("title", post.getTitle());
                params.put("content", post.getContent());
                params.put("id_post", post.getId_post().toString());
                params.put("imageA", strEncodedImageA);
                params.put("imageB", strEncodedImageB);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);

    }

    public void deletePost(Context context, String url) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        long deleted = 0;

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            deleted = jsonObject.getLong("deleted");
                        } catch (Exception e) {
                            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                        } finally {
                            if (deleted > 0) {
                                Intent intent = new Intent(context, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                context.startActivity(intent);
                                ((Activity)context).finish();
                            }
                            else {
                                Toast.makeText(context, "Error al borrar post", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveDraft(Context context, String url, Post post) {

        String encodedStringA, encodedStringB, strEncodedImageA, strEncodedImageB;

        if (post.getImageA() != null) {
            encodedStringA = Base64.getEncoder().encodeToString(post.getImageA());
            strEncodedImageA = "data:image/png;base64," + encodedStringA;
        } else {
            strEncodedImageA = "";
        }

        if (post.getImageB() != null) {
            encodedStringB = Base64.getEncoder().encodeToString(post.getImageB());
            strEncodedImageB = "data:image/png;base64," + encodedStringB;
        } else {
            strEncodedImageB = "";
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        long drafted = 0;

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            drafted = jsonObject.getLong("drafted");
                        } catch (Exception e) {
                            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                        } finally {
                            if (drafted > 0) {
                                Intent intent = new Intent(context, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                context.startActivity(intent);
                                ((Activity)context).finish();
                            }
                            else {
                                Toast.makeText(context, "Error al guardar borrador", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("title", post.getTitle());
                params.put("content", post.getContent());
                params.put("id_user", post.getId_user().toString());
                params.put("imageA", strEncodedImageA);
                params.put("imageB", strEncodedImageB);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);

    }

    public void updateDraft(Context context, String url, long id_post) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        long updated = 0;

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            updated = jsonObject.getLong("updated");
                        } catch (Exception e) {
                            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                        } finally {
                            if (updated > 0) {
                                Intent intent = new Intent(context, PostActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                intent.putExtra("IdPost", id_post);
                                context.startActivity(intent);
                                ((Activity)context).finish();
                            }
                            else {
                                Toast.makeText(context, "Error al subir borrador", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);

    }

    public void createReply(Context context, String url, Reply reply) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        long reply_id = -1;

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            reply_id = jsonObject.getLong("idReplyCreated");
                        } catch (Exception e) {
                            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                        } finally {
                            if (reply_id == -1) {
                                Toast.makeText(context, "Error al crear reply", Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("content", reply.getContent());
                params.put("id_user", reply.getId_user().toString());
                params.put("id_post", reply.getId_post().toString());
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);

    }

    public void deleteReply(Context context, String url) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        long deleted = 0;

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            deleted = jsonObject.getLong("deleted");
                        } catch (Exception e) {
                            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                        } finally {
                            if (deleted == 0) {
                                Toast.makeText(context, "Error al borrar reply", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);

    }

}
