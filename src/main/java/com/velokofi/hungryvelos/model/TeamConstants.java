package com.velokofi.hungryvelos.model;

public interface TeamConstants {

    int INDIVIDUAL_TARGET = 250;

    String TEAMS_CSV = """
            team_id, name, captain_strava_id
            1, In it to spin it, 61999014
            2, Avarthas, 65392239
            3, Velocopains, 14463022
            """;

    String TEAM_MEMBERS_CSV = """
            strava_id, name, gender, team_id, captain
            61999014, Vidyaprasanna Kambalur, M, 1, true
            62516649, Anil Kumar, M, 1, false
            67288162, harsha gandhi, M, 1, false
            38684955, Premchand Ryali, M, 1, false
            3225831, Prashanth S, M, 1, false
            64757576, Bharadwaja S R, M, 1, false
            73629852, Bharathi Malavalli, F, 1, false
            73044081, Ashok Nagesh, M, 1, false
            74126724, sunil achar, M, 1, false
            65392239, Vipin Devis, M, 2, true
            12920635, Sukumar Sundaram, M, 2, false
            35940578, Sai Srinivas, M, 2, false
            38148750, Chandrakanth K, M, 2, false
            64907699, amarnath vali, M, 2, false
            9125629, Deepu Scorpio, M, 2, false
            74963850, Nandini Kishor, F, 2, false
            74710582, Zainab Shoaib, F, 2, false
            25409226, Lakshmi Narasu, F, 2, false
            14463022, Sagar MC, M, 3, true
            37177283, Manjunath Sathyanarayana, M, 3, false
            69757350, Ravi Sunderrajan, M, 3, false
            28550167, Sudarshan Gopinath, M, 3, false
            65820756, Nanda Kishor, M, 3, false
            36760093, Manasa Bharadwaj, F, 3, false
            15589851, Sun'J Sharma, M, 3, false
            4629973, Ranjan Kulkarni, M, 3, false
            """;
}
