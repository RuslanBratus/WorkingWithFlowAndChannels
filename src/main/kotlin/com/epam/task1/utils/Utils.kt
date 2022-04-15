package com.epam.task1.utils

fun log(v: Any) = println("[${Thread.currentThread().name}] $v")

fun Float.format(digits: Int): String = String.format("%.${digits}f", this)

const val ORDER_DESK = "orderDesk"
const val NAME = "Car factory"
const val BODY_LINE_1 = "Body line 1"
const val BODY_LINE_2 = "Body line 2"
const val EQUIPMENT_LINE_1 = "Equipment line 1"
const val EQUIPMENT_LINE_2 = "Equipment line 2"
const val PROVIDED_BY_CONSTRUCTOR_TEAM_1 = "Provided by constructor team 1"
const val PROVIDED_BY_CONSTRUCTOR_TEAM_2 = "Provided by constructor team 2"
const val COMBINED_BODY_LINE_1 = "combineBody bodyLine 1"
const val COMBINED_BODY_LINE_2 = "combineBody bodyLine 2"
const val COMBINED_EQUIPMENT_LINE_1 = "combineEquipment equipmentLine 1"
const val COMBINED_EQUIPMENT_LINE_2 = "combineEquipment equipmentLine 2"
