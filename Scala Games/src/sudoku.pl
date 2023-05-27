:- use_module(library(clpfd)).

% Main predicate to solve Sudoku
solve_sudoku(Board, Solution) :-
    length(Board, 9),  % Ensure the board has 9 rows
    maplist(same_length(Board), Board),  % Ensure each row has 9 columns
    append(Board, Vs),  % Flatten the board
    Vs ins 1..9,  % Restrict values to be between 1 and 9
    maplist(all_distinct, Board),  % All values in each row must be distinct
    transpose(Board, Columns),  % Transpose the board to get columns
    maplist(all_distinct, Columns),  % All values in each column must be distinct
    Board = [R1, R2, R3, R4, R5, R6, R7, R8, R9],
    blocks(R1, R2, R3),
    blocks(R4, R5, R6),
    blocks(R7, R8, R9),
    label(Vs),  % Assign values to variables
    Solution = Board.

% Helper predicate to handle 3x3 blocks
% This recursive call works as follows...
% first line is base case, ABC are the first 3 elements of first row..
% DEF are second row of first block.. so one call of blocks checks distinction of 3x3 block
% called recursively on the remaining elements of R1 R2 R3 until there is no more to check
blocks([], [], []).
blocks([A, B, C | Bs1], [D, E, F | Bs2], [G, H, I | Bs3]) :-
    all_distinct([A, B, C, D, E, F, G, H, I]),
    blocks(Bs1, Bs2, Bs3).
% aw
