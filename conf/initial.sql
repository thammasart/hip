create table attention_blink_answer (
  id                        bigint not null auto_increment,
  used_time                 double,
  answer                    boolean,
  is_correct                boolean,
  user_username             varchar(20),
  quiz_id                   bigint,
  constraint pk_attention_blink_answer primary key (id))
;

create table stroop_answer (
  id                        bigint not null auto_increment,
  answer                    varchar(20),
  used_time                 double,
  user_username             varchar(20),
  quiz_id                   bigint,
  constraint pk_stroop_answer primary key (id))
;

create table sternberg_search_answer (
  id                        bigint not null auto_increment,
  answer                    boolean,
  used_time                 double,
  is_correct                boolean,
  user_username             varchar(20),
  quiz_id                   bigint,
  constraint pk_sternberg_search_answer primary key (id))
;

create table position_error_answer (
  id                        bigint not null auto_increment,
  answer                    varchar(255),
  used_time                 double,
  is_correct                boolean,
  user_username             varchar(20),
  quiz_id                   bigint,
  constraint pk_position_error_answer primary key (id))
;

create table signal_detection_answer (
  id                        bigint not null auto_increment,
  answer                    boolean,
  used_time                 double,
  is_correct                boolean,
  user_username             varchar(20),
  quiz_id                   bigint,
  constraint pk_signal_detection_answer primary key (id))
;

create table brown_peterson_answer (
  id                        bigint not null auto_increment,
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

create table experiment_schedule (
  id                        bigint not null auto_increment,
  name                      varchar(30) not null,
  no_of_trial               integer(2) not null,
  start_date                timestamp not null,
  expire_date               timestamp not null,
  experiment_type           integer,
  constraint ck_experiment_schedule_experiment_type check (experiment_type in (0,1,2,3,4,5)),
  constraint pk_experiment_schedule primary key (id))
;

create table attention_blink_question (
  id                        bigint not null auto_increment,
  letter                    varchar(255) not null,
  question_set              varchar(20) not null,
  correct_answer            boolean,
  question_type             integer,
  constraint ck_attention_blink_question_question_type check (question_type in (0,1,2)),
  constraint pk_attention_blink_question primary key (id))
;

create table brown_peterson_question (
  id                        bigint not null auto_increment,
  first_word                varchar(20) not null,
  second_word               varchar(20) not null,
  third_word                varchar(20) not null,
  trigram_type              varchar(255),
  trigram_language          varchar(255),
  constraint pk_brown_peterson_question primary key (id))
;

create table position_error_question (
  id                        bigint not null auto_increment,
  memory_set                varchar(255),
  question_type             integer,
  constraint ck_position_error_question_question_type check (question_type in (0,1,2)),
  constraint pk_position_error_question primary key (id))
;

create table stroop_engword_question (
  id                        bigint not null auto_increment,
  color_word                varchar(20) not null,
  ink_color                 varchar(20) not null,
  question_type             integer,
  constraint ck_stroop_engword_question_question_type check (question_type in (0,1,2)),
  constraint pk_stroop_engword_question primary key (id))
;

create table sternberg_search_question (
  id                        bigint not null auto_increment,
  question                  varchar(255),
  question_type             integer,
  quiz_id                   bigint,
  constraint ck_sternberg_search_question_question_type check (question_type in (0,1,2)),
  constraint pk_sternberg_search_question primary key (id))
;

create table signal_detection_question (
  id                        bigint not null auto_increment,
  target                    varchar(255),
  noise                     varchar(255),
  constraint pk_signal_detection_question primary key (id))
;

create table stroop_quiz (
  id                        bigint not null auto_increment,
  trial_id                  bigint,
  question_id               bigint,
  constraint pk_stroop_quiz primary key (id))
;

create table attention_blink_quiz (
  id                        bigint not null auto_increment,
  length                    integer,
  number_of_target          integer,
  blink_time                double,
  is_correct                boolean,
  trial_id                  bigint,
  question_id               bigint,
  constraint pk_attention_blink_quiz primary key (id))
;

create table position_error_quiz (
  id                        bigint not null auto_increment,
  length                    integer,
  question_id               bigint,
  trial_id                  bigint,
  constraint pk_position_error_quiz primary key (id))
;

create table brown_peterson_quiz (
  id                        bigint not null auto_increment,
  init_countdown            integer(3),
  flash_time                integer(20),
  trial_id                  bigint,
  question_id               bigint,
  constraint pk_brown_peterson_quiz primary key (id))
;

create table signal_detection_quiz (
  id                        bigint not null auto_increment,
  display_time              double,
  no_of_target              integer,
  length                    integer,
  question_id               bigint,
  trial_id                  bigint,
  constraint pk_signal_detection_quiz primary key (id))
;

create table sternberg_search_quiz (
  id                        bigint not null auto_increment,
  is_true                   boolean,
  trial_id                  bigint,
  question_id               bigint,
  constraint pk_sternberg_search_quiz primary key (id))
;

create table time_log (
  id                        bigint not null auto_increment,
  start_time                timestamp,
  end_time                  timestamp,
  user_username             varchar(20),
  trial_id                  bigint,
  exp_id                    bigint,
  constraint pk_time_log primary key (id))
;

create table stroop_time_log (
  id                        bigint not null auto_increment,
  start_time                timestamp,
  end_time                  timestamp,
  user_username             varchar(20),
  trial_id                  bigint,
  constraint pk_stroop_time_log primary key (id))
;

create table signal_trial (
  id                        bigint not null auto_increment,
  schedule_id               bigint,
  constraint pk_signal_trial primary key (id))
;

create table stroop_trial (
  id                        bigint not null auto_increment,
  appear_time               bigint(2) not null,
  question_type             integer(20) not null,
  schedule_id               bigint,
  constraint ck_stroop_trial_question_type check (question_type in (0,1,2)),
  constraint pk_stroop_trial primary key (id))
;

create table sternberg_search_trial (
  id                        bigint not null auto_increment,
  memory_set                varchar(255),
  length                    integer,
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

create table brown_peterson_trial (
  id                        bigint not null auto_increment,
  trigram_type              varchar(20) not null,
  trigram_language          varchar(20) not null,
  schedule_id               bigint,
  constraint pk_brown_peterson_trial primary key (id))
;

create table attention_blink_trial (
  id                        bigint not null auto_increment,
  question_type             integer,
  number_of_quiz            integer,
  schedule_id               bigint,
  constraint ck_attention_blink_trial_question_type check (question_type in (0,1,2)),
  constraint pk_attention_blink_trial primary key (id))
;

create table position_error_trial (
  id                        bigint not null auto_increment,
  flash_speed               double,
  delay_time                double,
  question_type             integer,
  schedule_id               bigint,
  constraint ck_position_error_trial_question_type check (question_type in (0,1,2)),
  constraint pk_position_error_trial primary key (id))
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
  constraint ck_user_status check (status in (0,1,2,3)),
  constraint pk_user primary key (username))
;

alter table attention_blink_answer add constraint fk_attention_blink_answer_user_1 foreign key (user_username) references user (username) on delete restrict on update restrict;
create index ix_attention_blink_answer_user_1 on attention_blink_answer (user_username);
alter table attention_blink_answer add constraint fk_attention_blink_answer_quiz_2 foreign key (quiz_id) references attention_blink_quiz (id) on delete restrict on update restrict;
create index ix_attention_blink_answer_quiz_2 on attention_blink_answer (quiz_id);
alter table stroop_answer add constraint fk_stroop_answer_user_3 foreign key (user_username) references user (username) on delete restrict on update restrict;
create index ix_stroop_answer_user_3 on stroop_answer (user_username);
alter table stroop_answer add constraint fk_stroop_answer_quiz_4 foreign key (quiz_id) references stroop_quiz (id) on delete restrict on update restrict;
create index ix_stroop_answer_quiz_4 on stroop_answer (quiz_id);
alter table sternberg_search_answer add constraint fk_sternberg_search_answer_use_5 foreign key (user_username) references user (username) on delete restrict on update restrict;
create index ix_sternberg_search_answer_use_5 on sternberg_search_answer (user_username);
alter table sternberg_search_answer add constraint fk_sternberg_search_answer_qui_6 foreign key (quiz_id) references sternberg_search_quiz (id) on delete restrict on update restrict;
create index ix_sternberg_search_answer_qui_6 on sternberg_search_answer (quiz_id);
alter table position_error_answer add constraint fk_position_error_answer_user_7 foreign key (user_username) references user (username) on delete restrict on update restrict;
create index ix_position_error_answer_user_7 on position_error_answer (user_username);
alter table position_error_answer add constraint fk_position_error_answer_quiz_8 foreign key (quiz_id) references position_error_quiz (id) on delete restrict on update restrict;
create index ix_position_error_answer_quiz_8 on position_error_answer (quiz_id);
alter table signal_detection_answer add constraint fk_signal_detection_answer_use_9 foreign key (user_username) references user (username) on delete restrict on update restrict;
create index ix_signal_detection_answer_use_9 on signal_detection_answer (user_username);
alter table signal_detection_answer add constraint fk_signal_detection_answer_qu_10 foreign key (quiz_id) references signal_detection_quiz (id) on delete restrict on update restrict;
create index ix_signal_detection_answer_qu_10 on signal_detection_answer (quiz_id);
alter table brown_peterson_answer add constraint fk_brown_peterson_answer_user_11 foreign key (user_username) references user (username) on delete restrict on update restrict;
create index ix_brown_peterson_answer_user_11 on brown_peterson_answer (user_username);
alter table brown_peterson_answer add constraint fk_brown_peterson_answer_quiz_12 foreign key (quiz_id) references brown_peterson_quiz (id) on delete restrict on update restrict;
create index ix_brown_peterson_answer_quiz_12 on brown_peterson_answer (quiz_id);
alter table sternberg_search_question add constraint fk_sternberg_search_question__13 foreign key (quiz_id) references sternberg_search_quiz (id) on delete restrict on update restrict;
create index ix_sternberg_search_question__13 on sternberg_search_question (quiz_id);
alter table stroop_quiz add constraint fk_stroop_quiz_trial_14 foreign key (trial_id) references stroop_trial (id) on delete restrict on update restrict;
create index ix_stroop_quiz_trial_14 on stroop_quiz (trial_id);
alter table stroop_quiz add constraint fk_stroop_quiz_question_15 foreign key (question_id) references stroop_engword_question (id) on delete restrict on update restrict;
create index ix_stroop_quiz_question_15 on stroop_quiz (question_id);
alter table attention_blink_quiz add constraint fk_attention_blink_quiz_trial_16 foreign key (trial_id) references attention_blink_trial (id) on delete restrict on update restrict;
create index ix_attention_blink_quiz_trial_16 on attention_blink_quiz (trial_id);
alter table attention_blink_quiz add constraint fk_attention_blink_quiz_quest_17 foreign key (question_id) references attention_blink_question (id) on delete restrict on update restrict;
create index ix_attention_blink_quiz_quest_17 on attention_blink_quiz (question_id);
alter table position_error_quiz add constraint fk_position_error_quiz_questi_18 foreign key (question_id) references position_error_question (id) on delete restrict on update restrict;
create index ix_position_error_quiz_questi_18 on position_error_quiz (question_id);
alter table position_error_quiz add constraint fk_position_error_quiz_trial_19 foreign key (trial_id) references position_error_trial (id) on delete restrict on update restrict;
create index ix_position_error_quiz_trial_19 on position_error_quiz (trial_id);
alter table brown_peterson_quiz add constraint fk_brown_peterson_quiz_trial_20 foreign key (trial_id) references brown_peterson_trial (id) on delete restrict on update restrict;
create index ix_brown_peterson_quiz_trial_20 on brown_peterson_quiz (trial_id);
alter table brown_peterson_quiz add constraint fk_brown_peterson_quiz_questi_21 foreign key (question_id) references brown_peterson_question (id) on delete restrict on update restrict;
create index ix_brown_peterson_quiz_questi_21 on brown_peterson_quiz (question_id);
alter table signal_detection_quiz add constraint fk_signal_detection_quiz_ques_22 foreign key (question_id) references signal_detection_question (id) on delete restrict on update restrict;
create index ix_signal_detection_quiz_ques_22 on signal_detection_quiz (question_id);
alter table signal_detection_quiz add constraint fk_signal_detection_quiz_tria_23 foreign key (trial_id) references signal_trial (id) on delete restrict on update restrict;
create index ix_signal_detection_quiz_tria_23 on signal_detection_quiz (trial_id);
alter table sternberg_search_quiz add constraint fk_sternberg_search_quiz_tria_24 foreign key (trial_id) references sternberg_search_trial (id) on delete restrict on update restrict;
create index ix_sternberg_search_quiz_tria_24 on sternberg_search_quiz (trial_id);
alter table sternberg_search_quiz add constraint fk_sternberg_search_quiz_ques_25 foreign key (question_id) references sternberg_search_question (id) on delete restrict on update restrict;
create index ix_sternberg_search_quiz_ques_25 on sternberg_search_quiz (question_id);
alter table time_log add constraint fk_time_log_user_26 foreign key (user_username) references user (username) on delete restrict on update restrict;
create index ix_time_log_user_26 on time_log (user_username);
alter table time_log add constraint fk_time_log_exp_27 foreign key (exp_id) references experiment_schedule (id) on delete restrict on update restrict;
create index ix_time_log_exp_27 on time_log (exp_id);
alter table stroop_time_log add constraint fk_stroop_time_log_user_28 foreign key (user_username) references user (username) on delete restrict on update restrict;
create index ix_stroop_time_log_user_28 on stroop_time_log (user_username);
alter table stroop_time_log add constraint fk_stroop_time_log_trial_29 foreign key (trial_id) references stroop_trial (id) on delete restrict on update restrict;
create index ix_stroop_time_log_trial_29 on stroop_time_log (trial_id);
alter table signal_trial add constraint fk_signal_trial_schedule_30 foreign key (schedule_id) references experiment_schedule (id) on delete restrict on update restrict;
create index ix_signal_trial_schedule_30 on signal_trial (schedule_id);
alter table stroop_trial add constraint fk_stroop_trial_schedule_31 foreign key (schedule_id) references experiment_schedule (id) on delete restrict on update restrict;
create index ix_stroop_trial_schedule_31 on stroop_trial (schedule_id);
alter table sternberg_search_trial add constraint fk_sternberg_search_trial_sch_32 foreign key (schedule_id) references experiment_schedule (id) on delete restrict on update restrict;
create index ix_sternberg_search_trial_sch_32 on sternberg_search_trial (schedule_id);
alter table brown_peterson_trial add constraint fk_brown_peterson_trial_sched_33 foreign key (schedule_id) references experiment_schedule (id) on delete restrict on update restrict;
create index ix_brown_peterson_trial_sched_33 on brown_peterson_trial (schedule_id);
alter table attention_blink_trial add constraint fk_attention_blink_trial_sche_34 foreign key (schedule_id) references experiment_schedule (id) on delete restrict on update restrict;
create index ix_attention_blink_trial_sche_34 on attention_blink_trial (schedule_id);
alter table position_error_trial add constraint fk_position_error_trial_sched_35 foreign key (schedule_id) references experiment_schedule (id) on delete restrict on update restrict;
create index ix_position_error_trial_sched_35 on position_error_trial (schedule_id);

insert into user (username,password,status) values ('admin', 'admin', 0);
update user set first_name='admin',last_name='admin',gender='',section='',semester='',academic_year='',year=2555,e_mail='',faculty='', department='';