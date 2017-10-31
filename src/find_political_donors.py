
# coding: utf-8

# In[1]:


import pandas as pd


# ## Parse and clean
# 
# If your solution requires additional libraries, environments, or dependencies, you must specify these in your README documentation. See the figure below for the required structure of the top-most directory in your repo, or simply clone this repo.
# 
# Links:
# 1. Clear iPython vars: https://stackoverflow.com/questions/22934204/how-to-clear-variables-in-ipythonS
# 2. Running median for static array: https://www.youtube.com/watch?v=VmogG01IjYc
# 
# Possible optimizations:
# 1. Reading data in chunks: http://pandas-docs.github.io/pandas-docs-travis/io.html#iterating-through-files-chunk-by-chunk
# 2. Parent link for point 1: https://stackoverflow.com/questions/14262433/large-data-work-flows-using-pandas
# 3. Running median for streaming chunks of data: https://stackoverflow.com/questions/10657503/find-running-median-from-a-stream-of-integers

# In[2]:


# Read in header file
column_names = pd.read_csv(filepath_or_buffer='./indiv_header_file.csv')
print(column_names)


# In[3]:


# Read in input file, attach header for column names
itcont_df = pd.read_csv(filepath_or_buffer='../input/itcont.txt', sep='|', header=None, names=list(column_names))
print(itcont_df.head())


# In[4]:


relevant_column_names = ['CMTE_ID', 'ZIP_CODE', 'TRANSACTION_DT', 'TRANSACTION_AMT', 'OTHER_ID'];


# In[5]:


# Drop irrelevant columns
itcont_df = itcont_df[relevant_column_names]
print(itcont_df.head())


# In[6]:


# Remove entries w/OTHER_ID set
itcont_df = itcont_df[pd.isnull(itcont_df['OTHER_ID'])]
print(itcont_df)

