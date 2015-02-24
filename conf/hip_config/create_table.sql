create table brown_peterson_answer (
  id                        bigint not null AUTO_INCREMENT,
  first_word                varchar(20),
  second_word               varchar(20),
  third_word                varchar(20),
  used_time                 double,
  countdown_result          varchar(20),
  is_correct                boolean,
  user_username             varchar(20),
  quiz_id                   bigint,
  constraint pk_brown_peterson_answer primary key (id))
;

create table muller_layer_answer (
  id                        bigint not null AUTO_INCREMENT,
  answer                    integer,
  used_time                 double,
  is_correct                boolean,
  user_username             varchar(20),
  quiz_id                   bigint,
  constraint pk_muller_layer_answer primary key (id))
;

create table simon_effect_answer (
  id                        bigint not null AUTO_INCREMENT,
  answer                    varchar(255),
  used_time                 double,
  is_correct                boolean,
  user_username             varchar(20),
  quiz_id                   bigint,
  constraint pk_simon_effect_answer primary key (id))
;

create table attention_blink_answer (
  id                        bigint not null AUTO_INCREMENT,
  used_time                 double,
  answer                    boolean,
  is_correct                boolean,
  user_username             varchar(20),
  quiz_id                   bigint,
  constraint pk_attention_blink_answer primary key (id))
;

create table garner_interference_answer (
  id                        bigint not null AUTO_INCREMENT,
  answer                    boolean,
  used_time                 double,
  is_correct                boolean,
  user_username             varchar(20),
  quiz_id                   bigint,
  constraint pk_garner_interference_answer primary key (id))
;

create table stroop_answer (
  id                        bigint not null AUTO_INCREMENT,
  answer                    varchar(20),
  used_time                 double,
  is_correct                boolean,
  user_username             varchar(20),
  quiz_id                   bigint,
  constraint pk_stroop_answer primary key (id))
;

create table visual_search_answer (
  id                        bigint not null AUTO_INCREMENT,
  position_x                integer,
  position_y                integer,
  used_time                 double,
  is_correct                boolean,
  user_username             varchar(20),
  quiz_id                   bigint,
  constraint pk_visual_search_answer primary key (id))
;

create table sternberg_search_answer (
  id                        bigint not null AUTO_INCREMENT,
  answer                    boolean,
  used_time                 double,
  is_correct                boolean,
  user_username             varchar(20),
  quiz_id                   bigint,
  constraint pk_sternberg_search_answer primary key (id))
;

create table position_error_answer (
  id                        bigint not null AUTO_INCREMENT,
  answer                    varchar(255),
  used_time                 double,
  is_correct                boolean,
  user_username             varchar(20),
  quiz_id                   bigint,
  constraint pk_position_error_answer primary key (id))
;

create table magic_number_7_answer (
  id                        bigint not null AUTO_INCREMENT,
  answer                    varchar(255),
  used_time                 double,
  score                     integer,
  user_username             varchar(20),
  quiz_id                   bigint,
  constraint pk_magic_number_7_answer primary key (id))
;

create table signal_detection_answer (
  id                        bigint not null AUTO_INCREMENT,
  answer                    boolean,
  used_time                 double,
  is_correct                boolean,
  user_username             varchar(20),
  quiz_id                   bigint,
  constraint pk_signal_detection_answer primary key (id))
;

create table change_blindness_answer (
  id                        bigint not null AUTO_INCREMENT,
  used_time                 double,
  position_of_change_x      double,
  position_of_change_y      double,
  is_correct                boolean,
  user_username             varchar(20),
  quiz_id                   bigint,
  constraint pk_change_blindness_answer primary key (id))
;

create table garner_interference_color (
  id                        bigint not null AUTO_INCREMENT,
  color_code                varchar(255),
  color                     varchar(255),
  saturation                integer,
  constraint pk_garner_interference_color primary key (id))
;

create table experiment_schedule (
  id                        bigint not null AUTO_INCREMENT,
  name                      varchar(30) not null,
  no_of_trial               integer(2) not null,
  start_date                timestamp not null,
  expire_date               timestamp not null,
  experiment_type           integer,
  constraint ck_experiment_schedule_experiment_type check (experiment_type in (0,1,2,3,4,5,6,7,8,9,10,11)),
  constraint pk_experiment_schedule primary key (id))
;

create table brown_peterson_question (
  id                        bigint not null AUTO_INCREMENT,
  first_word                varchar(20) not null,
  second_word               varchar(20) not null,
  third_word                varchar(20) not null,
  trigram_type              varchar(255),
  trigram_language          varchar(255),
  constraint pk_brown_peterson_question primary key (id))
;

create table stroop_engword_question (
  id                        bigint not null AUTO_INCREMENT,
  color_word                varchar(20) not null,
  ink_color                 varchar(20) not null,
  question_type             integer,
  constraint ck_stroop_engword_question_question_type check (question_type in (0,1,2)),
  constraint pk_stroop_engword_question primary key (id))
;

create table sternberg_search_question (
  id                        bigint not null AUTO_INCREMENT,
  memory_set                varchar(255),
  question_type             integer,
  constraint ck_sternberg_search_question_question_type check (question_type in (0,1,2)),
  constraint pk_sternberg_search_question primary key (id))
;

create table change_blindness_question (
  id                        bigint not null AUTO_INCREMENT,
  path_of_pic1              varchar(255),
  path_of_pic2              varchar(255),
  answer_area_width         double,
  answer_area_height        double,
  position_of_change_x      double,
  position_of_change_y      double,
  constraint pk_change_blindness_question primary key (id))
;

create table magic_number_7_question (
  id                        bigint not null AUTO_INCREMENT,
  memory_set                varchar(255),
  question_type             integer,
  constraint ck_magic_number_7_question_question_type check (question_type in (0,1,2)),
  constraint pk_magic_number_7_question primary key (id))
;

create table muller_layer_question (
  id                        bigint not null AUTO_INCREMENT,
  line1                     integer,
  line2                     integer,
  line3                     integer,
  line4                     integer,
  line5                     integer,
  constraint ck_muller_layer_question_line1 check (line1 in (0,1,2,3,4)),
  constraint ck_muller_layer_question_line2 check (line2 in (0,1,2,3,4)),
  constraint ck_muller_layer_question_line3 check (line3 in (0,1,2,3,4)),
  constraint ck_muller_layer_question_line4 check (line4 in (0,1,2,3,4)),
  constraint ck_muller_layer_question_line5 check (line5 in (0,1,2,3,4)),
  constraint pk_muller_layer_question primary key (id))
;

create table attention_blink_question (
  id                        bigint not null AUTO_INCREMENT,
  letter                    varchar(255) not null,
  question_set              varchar(20) not null,
  correct_answer            boolean,
  question_type             integer,
  constraint ck_attention_blink_question_question_type check (question_type in (0,1,2)),
  constraint pk_attention_blink_question primary key (id))
;

create table position_error_question (
  id                        bigint not null AUTO_INCREMENT,
  memory_set                varchar(255),
  question_type             integer,
  constraint ck_position_error_question_question_type check (question_type in (0,1,2)),
  constraint pk_position_error_question primary key (id))
;

create table simon_effect_question (
  id                        bigint not null AUTO_INCREMENT,
  color                     varchar(255),
  alphabet                  varchar(255),
  direction                 varchar(255),
  constraint pk_simon_effect_question primary key (id))
;

create table garner_interference_question (
  id                        bigint not null AUTO_INCREMENT,
  color_pic                 varchar(255),
  size_pic                  varchar(255),
  color_question            varchar(255),
  size_question             varchar(255),
  color_match               boolean,
  size_match                boolean,
  constraint pk_garner_interference_question primary key (id))
;

create table signal_detection_question (
  id                        bigint not null AUTO_INCREMENT,
  target                    varchar(255),
  noise                     varchar(255),
  constraint pk_signal_detection_question primary key (id))
;

create table visual_search_question (
  id                        bigint not null AUTO_INCREMENT,
  sharps                    TEXT,
  constraint pk_visual_search_question primary key (id))
;

create table garner_interference_quiz (
  id                        bigint not null AUTO_INCREMENT,
  question_type             integer,
  trial_id                  bigint,
  question_id               bigint,
  constraint ck_garner_interference_quiz_question_type check (question_type in (0,1,2)),
  constraint pk_garner_interference_quiz primary key (id))
;

create table simon_effect_quiz (
  id                        bigint not null AUTO_INCREMENT,
  position                  varchar(255),
  trial_id                  bigint,
  question_id               bigint,
  constraint pk_simon_effect_quiz primary key (id))
;

create table visual_search_quiz (
  id                        bigint not null AUTO_INCREMENT,
  position_xof_target       double,
  position_yof_target       double,
  target                    integer,
  square_blue               integer,
  square_green              integer,
  square_red                integer,
  circle_green              integer,
  circle_red                integer,
  circle_blue               integer,
  frame_size                integer,
  question_id               bigint,
  trial_id                  bigint,
  constraint ck_visual_search_quiz_target check (target in (0,1,2,3,4,5)),
  constraint ck_visual_search_quiz_frame_size check (frame_size in (0,1,2,3,4)),
  constraint pk_visual_search_quiz primary key (id))
;

create table change_blindness_quiz (
  id                        bigint not null AUTO_INCREMENT,
  trial_id                  bigint,
  question_id               bigint,
  constraint pk_change_blindness_quiz primary key (id))
;

create table muller_layer_Quiz (
  id                        bigint not null AUTO_INCREMENT,
  no_of_choice              integer,
  differ_choice             integer,
  length_type               integer,
  differ_length             float,
  is_positive               boolean,
  trial_id                  bigint,
  question_id               bigint,
  constraint ck_muller_layer_Quiz_length_type check (length_type in (0,1,2)),
  constraint pk_muller_layer_Quiz primary key (id))
;

create table stroop_quiz (
  id                        bigint not null AUTO_INCREMENT,
  trial_id                  bigint,
  question_id               bigint,
  constraint pk_stroop_quiz primary key (id))
;

create table signal_detection_quiz (
  id                        bigint not null AUTO_INCREMENT,
  display_time              double,
  no_of_target              integer,
  length                    integer,
  question_id               bigint,
  trial_id                  bigint,
  constraint pk_signal_detection_quiz primary key (id))
;

create table sternberg_search_quiz (
  id                        bigint not null AUTO_INCREMENT,
  question_char             varchar(255),
  is_true                   boolean,
  trial_id                  bigint,
  question_id               bigint,
  constraint pk_sternberg_search_quiz primary key (id))
;

create table magic_number_7_quiz (
  id                        bigint not null AUTO_INCREMENT,
  display_time              double,
  chunk_size                integer,
  length                    integer,
  trial_id                  bigint,
  question_id               bigint,
  constraint pk_magic_number_7_quiz primary key (id))
;

create table attention_blink_quiz (
  id                        bigint not null AUTO_INCREMENT,
  length                    integer,
  number_of_target          integer,
  blink_time                double,
  is_correct                boolean,
  trial_id                  bigint,
  question_id               bigint,
  constraint pk_attention_blink_quiz primary key (id))
;

create table brown_peterson_quiz (
  id                        bigint not null AUTO_INCREMENT,
  init_countdown            integer(3),
  flash_time                integer(20),
  trial_id                  bigint,
  question_id               bigint,
  constraint pk_brown_peterson_quiz primary key (id))
;

create table position_error_quiz (
  id                        bigint not null AUTO_INCREMENT,
  length                    integer,
  question_id               bigint,
  trial_id                  bigint,
  constraint pk_position_error_quiz primary key (id))
;

create table time_log (
  id                        bigint not null AUTO_INCREMENT,
  start_time                timestamp,
  end_time                  timestamp,
  user_username             varchar(20),
  trial_id                  bigint,
  exp_id                    bigint,
  constraint pk_time_log primary key (id))
;

create table signal_detection_trial (
  id                        bigint not null AUTO_INCREMENT,
  schedule_id               bigint,
  total_score               double,
  total_used_time           double,
  total_user                integer,
  constraint pk_signal_detection_trial primary key (id))
;

create table stroop_trial (
  id                        bigint not null AUTO_INCREMENT,
  appear_time               bigint(2),
  question_type             integer(20),
  total_score               double,
  total_used_time           double,
  total_user                integer,
  schedule_id               bigint,
  constraint ck_stroop_trial_question_type check (question_type in (0,1,2)),
  constraint pk_stroop_trial primary key (id))
;

create table visual_search_trial (
  id                        bigint not null AUTO_INCREMENT,
  schedule_id               bigint,
  total_score               double,
  total_used_time           double,
  total_user                integer,
  constraint pk_visual_search_trial primary key (id))
;

create table brown_peterson_trial (
  id                        bigint not null AUTO_INCREMENT,
  total_score               double,
  total_used_time           double,
  total_user                integer,
  trigram_type              varchar(20) not null,
  trigram_language          varchar(20) not null,
  schedule_id               bigint,
  constraint pk_brown_peterson_trial primary key (id))
;

create table attention_blink_trial (
  id                        bigint not null AUTO_INCREMENT,
  question_type             integer,
  number_of_quiz            integer,
  total_user                integer,
  total_score               double,
  total_used_time           double,
  schedule_id               bigint,
  constraint ck_attention_blink_trial_question_type check (question_type in (0,1,2)),
  constraint pk_attention_blink_trial primary key (id))
;

create table position_error_trial (
  id                        bigint not null AUTO_INCREMENT,
  flash_speed               double,
  delay_time                double,
  question_type             integer,
  total_score               double,
  total_used_time           double,
  total_user                integer,
  number_of_quiz            integer,
  schedule_id               bigint,
  constraint ck_position_error_trial_question_type check (question_type in (0,1,2)),
  constraint pk_position_error_trial primary key (id))
;

create table change_blindness_trial (
  id                        bigint not null AUTO_INCREMENT,
  display_time              integer,
  schedule_id               bigint,
  total_score               double,
  total_used_time           double,
  total_user                integer,
  constraint pk_change_blindness_trial primary key (id))
;

create table simon_effect_trial (
  id                        bigint not null AUTO_INCREMENT,
  question_type             integer,
  blink_time                double,
  no_of_quiz                integer,
  total_score               double,
  total_user                integer,
  total_used_time           double,
  schedule_id               bigint,
  constraint ck_simon_effect_trial_question_type check (question_type in (0,1)),
  constraint pk_simon_effect_trial primary key (id))
;

create table sternberg_search_trial (
  id                        bigint not null AUTO_INCREMENT,
  length                    integer,
  total_score               double,
  total_used_time           double,
  total_user                integer,
  blink_time                double,
  one_char_is_correct       integer,
  one_char_is_in_correct    integer,
  two_char_is_correct       integer,
  two_char_is_in_correct    integer,
  question_type             integer,
  schedule_id               bigint,
  constraint ck_sternberg_search_trial_question_type check (question_type in (0,1,2)),
  constraint pk_sternberg_search_trial primary key (id))
;

create table garner_interference_trial (
  id                        bigint not null AUTO_INCREMENT,
  total_score               double,
  total_used_time           double,
  total_user                integer,
  length_big_square         double,
  length_small_square       double,
  no_of_color_question      integer,
  no_of_fake_color_question integer,
  no_of_size_question       integer,
  no_of_fake_size_question  integer,
  no_of_bi_dimension_question integer,
  no_of_fake_bi_dimentsion_question integer,
  color                     varchar(255),
  feature                   integer,
  color_dark_id             bigint,
  color_light_id            bigint,
  schedule_id               bigint,
  constraint ck_garner_interference_trial_feature check (feature in (0,1)),
  constraint pk_garner_interference_trial primary key (id))
;

create table muller_layer_trial (
  id                        bigint not null AUTO_INCREMENT,
  no_of_quiz                integer,
  total_score               double,
  total_used_time           double,
  total_user                integer,
  schedule_id               bigint,
  constraint pk_muller_layer_trial primary key (id))
;

create table magic_number_7_trial (
  id                        bigint not null AUTO_INCREMENT,
  total_score               double,
  total_used_time           double,
  total_user                integer,
  question_type             integer,
  schedule_id               bigint,
  number_of_quiz            integer,
  constraint ck_magic_number_7_trial_question_type check (question_type in (0,1,2)),
  constraint pk_magic_number_7_trial primary key (id))
;

create table user (
  username                  varchar(20) not null,
  password                  varchar(20) not null,
  status                    integer,
  first_name                varchar(255),
  last_name                 varchar(255),
  gender                    varchar(255),
  birth_date                timestamp,
  section                   varchar(255),
  semester                  varchar(255),
  academic_year             varchar(255),
  year                      integer,
  e_mail                    varchar(255),
  faculty                   varchar(255),
  department                varchar(255),
  constraint ck_user_status check (status in (0,1,2,3,4,5,6)),
  constraint pk_user primary key (username))
;
