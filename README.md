# CircleProgressBar

![](http://i.imgur.com/3mmtrMc.png)

A circular gradient Android progress bar.


----------

# 开发文档 (Development Help)

## 引用方式

在build.gradle中添加如下引用：

		compile 'com.coder:circlebar:1.0.0'

## circlebar支持的功能

- 设置进度的最大值

		progress.setMaxstepnumber(100);
- 设置单色进度条

		progress.setColor(0xff568951);

- 设置渐变色

		int[] mColors = new int[]{0xFF123456, 0xFF369852, 0xFF147852};

    	progress.setShaderColor(mColors);

- 更新进度，设置动画时间

		progress.update(50, 3000);


----------

## 联系方式

邮箱：<shenjuex@gmail.com>

## 我的博客

[点击查看我的博客](http://blog.renleicoder.com/)
